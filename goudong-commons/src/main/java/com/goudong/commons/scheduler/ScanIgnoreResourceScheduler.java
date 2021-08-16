package com.goudong.commons.scheduler;

import com.google.common.collect.Lists;
import com.goudong.commons.annotation.IgnoreResource;
import com.goudong.commons.constant.BasePackageConst;
import com.goudong.commons.constant.CommonConst;
import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.enumerate.RequestMappingEnum;
import com.goudong.commons.exception.ServerException;
import com.goudong.commons.openfeign.Oauth2Service;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.StringUtil;
import com.goudong.commons.vo.BaseIgnoreResourceVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 类描述：
 * 扫描有@IgnoreResource的控制层方法，将接口放入到白名单
 * @Author msi
 * @Date 2021-08-14 8:49
 * @Version 1.0
 */
@Slf4j
@Component
public class ScanIgnoreResourceScheduler {

    @Autowired
    private Oauth2Service oauth2Service;


    @SneakyThrows
    @Scheduled(initialDelayString="${scheduler.ScanIgnoreResourceScheduler.initialDelay}", fixedRateString="${scheduler.ScanIgnoreResourceScheduler.fixedRate}")
    public void scheduler() {
        long var0 = System.currentTimeMillis();
        scanIgnoreResource();
        long var1 = System.currentTimeMillis();
        log.info("ScanIgnoreResourceScheduler 定时器执行花费时长: {} ms", var1 - var0);
    }

    /**
     * 扫描白名单资源
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private List<BaseIgnoreResourceDTO> scanIgnoreResource() throws IOException, ClassNotFoundException {
        final String RESOURCE_PATTERN = CommonConst.ALL_CLASSES_ANT_PATH;
        // 扫描的包名，注意：子模块只能扫描到自己的包下的class。
        String[] controllers = {
                BasePackageConst.OAUTH2_CONTROLLER,
                BasePackageConst.MESSAGE_CONTROLLER,
        };
        // 获取资源集合
        List<Resource> resources = generateResources(RESOURCE_PATTERN, controllers);
        // 解析资源集合，并返回白名单集合
        List<BaseIgnoreResourceDTO> ignoreResourceDTOS = generateIgnoreResources(resources);

        // 有符合条件的，就添加到数据。
        if (!ignoreResourceDTOS.isEmpty()) {
            List<BaseIgnoreResourceVO> ignoreResourcePOS = BeanUtil.copyList(ignoreResourceDTOS, BaseIgnoreResourceVO.class);

            // 插入数据库
            Result<List<BaseIgnoreResourceDTO>> result = oauth2Service.addList(ignoreResourcePOS);
            return result.getData();
        }

        return new ArrayList<>();
    }

    /**
     * 生产resource资源集合，用于后续扫描资源
     * @param RESOURCE_PATTERN 类路径下的位置
     * @param controllers 所有微服务的控制层包完全名数组
     * @return
     */
    private List<Resource> generateResources(String RESOURCE_PATTERN, String[] controllers) {
        // resource解析模式
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();
        Stream.of(controllers).forEach(p->{
            String pattern = new StringBuilder()
                    .append(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)
                    .append(ClassUtils.convertClassNameToResourcePath(p))
                    .append(RESOURCE_PATTERN).toString();
            try {
                resources.addAll(Lists.newArrayList(resourcePatternResolver.getResources(pattern)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return resources;
    }

    /**
     * 循环资源，找到白名单接口，将其请求路径和请求方法创建对象并放入集合返回。
     * @param resources 资源集合
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private List<BaseIgnoreResourceDTO> generateIgnoreResources(List<Resource> resources) throws IOException, ClassNotFoundException {

        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(new PathMatchingResourcePatternResolver());
        List<BaseIgnoreResourceDTO> ignoreResourceDTOS = new ArrayList<>();
        // 循环
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);

                //扫描到的class
                String className = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                Method[] methods = clazz.getDeclaredMethods();
                // 这里根据这个项目，我们约束：控制器类上的必须只能使用@RequestMapping注解来限制请求
                String[] prefixArr = clazz.getAnnotation(RequestMapping.class).value();

                for (Method method: methods) {
                    //判断是否有指定注解
                    IgnoreResource ignoreResource = method.getAnnotation(IgnoreResource.class);
                    if(ignoreResource != null){
                        // 备注信息
                        String remark = ignoreResource.value();
                        // 方法上的RequestMapping类型注解
                        Annotation httpMethodAnnotation = getHttpMethodAnnotation(method);

                        ignoreResourceDTOS.addAll(annotation2IgnoreResourceDTOS(httpMethodAnnotation, prefixArr, remark));
                    }
                }
            }
        }
        return ignoreResourceDTOS;
    }

    /**
     * 获取api接口的请求方式
     * @param method
     * @return
     */
    public Annotation getHttpMethodAnnotation(Method method){
        RequestMappingEnum[] values = RequestMappingEnum.values();
        for (int i = 0; i < values.length; i++) {
            Annotation annotation = method.getAnnotation(values[i].getMapping());
            if (annotation != null) {
                return annotation;
            }
        }
        String errorMessage = "方法："+ method.getName() +
                " 加了 @IgnoreResource，但是方法没加Mapping注解（如@RequestMapping、@GetMapping、@PostMapping、@PutMapping、@DeleteMapping等类似注解）";

        throw ServerException.serverException(errorMessage);
    }

    /**
     * 将接口请求注解转换成 白名单数据
     * @param annotation 注解（值可能是 @Requestmapping、@Getmapping、@PostMapping、@DeleteMapping、@PutMapping等）
     * @param prefixArr 控制器类上的注解，请求前缀
     * @param remark 备注
     * @return
     */
    private List<BaseIgnoreResourceDTO> annotation2IgnoreResourceDTOS (Annotation annotation, String[] prefixArr, String remark) {
        List<BaseIgnoreResourceDTO> var0 = new ArrayList<>();
        if (annotation instanceof RequestMapping) {
            RequestMapping requestMapping = (RequestMapping) annotation;
            String[] path = paths(requestMapping.value());
            RequestMethod[] method = requestMapping.method();
            for (int i = 0; i < path.length; i++) {
                var0.add(new BaseIgnoreResourceDTO(path[i], method[i].name(), remark));
            }
        }

        if (annotation instanceof GetMapping) {
            GetMapping requestMapping = (GetMapping) annotation;
            String[] path = paths(requestMapping.value());
            for (int i = 0; i < path.length; i++) {
                var0.add(new BaseIgnoreResourceDTO(path[i], HttpMethod.GET.name(), remark));
            }
        }

        if (annotation instanceof PostMapping) {
            PostMapping requestMapping = (PostMapping) annotation;
            String[] path = paths(requestMapping.value());
            for (int i = 0; i < path.length; i++) {
                var0.add(new BaseIgnoreResourceDTO(path[i], HttpMethod.POST.name(), remark));
            }
        }

        if (annotation instanceof PutMapping) {
            PutMapping requestMapping = (PutMapping) annotation;
            String[] path = paths(requestMapping.value());
            for (int i = 0; i < path.length; i++) {
                var0.add(new BaseIgnoreResourceDTO(path[i], HttpMethod.PUT.name(), remark));
            }
        }

        if (annotation instanceof DeleteMapping) {
            DeleteMapping requestMapping = (DeleteMapping) annotation;
            String[] path = paths(requestMapping.value());
            for (int i = 0; i < path.length; i++) {
                var0.add(new BaseIgnoreResourceDTO(path[i], HttpMethod.DELETE.name(), remark));
            }
        }

        if (annotation instanceof PatchMapping) {
            PatchMapping requestMapping = (PatchMapping) annotation;
            String[] path = paths(requestMapping.value());
            for (int i = 0; i < path.length; i++) {
                var0.add(new BaseIgnoreResourceDTO(path[i], HttpMethod.DELETE.name(), remark));
            }
        }

        // 控制器方法有路径前缀时
        if (prefixArr != null && prefixArr.length > 0) {
            List<BaseIgnoreResourceDTO> var1 = new ArrayList<>(prefixArr.length * var0.size());
            for (int i = 0; i < prefixArr.length; i++) {
                for (BaseIgnoreResourceDTO po : var0) {
                    // 拼接url
                    String url = CommonConst.API_PREFIX + prefixArr[i] + po.getPattern();
                    // 将url中的路径参数替换成 *
                    String pattern = StringUtil.replaceBracket(url, CommonConst.ASTERISK);
                    var1.add(new BaseIgnoreResourceDTO(pattern, po.getMethod(), remark));
                }
            }

            return var1;
        }

        // 控制器上没有路径前缀时
        for (BaseIgnoreResourceDTO po : var0) {
            // 加上前缀 ”/api/*“ 让后面校验请求时严格，并将{}替换成*
            String pattern = StringUtil.replaceBracket(CommonConst.API_PREFIX + po.getPattern(), CommonConst.ASTERISK);
            po.setPattern(pattern);
        }

        return var0;
    }

    /**
     * 接口没有定义path，那么就给默认 /
     * @param paths 请求地址数组
     * @return
     */
    private String[] paths(String[] paths) {
        if (paths.length > 0) {
            return paths;
        }
        return new String[]{CommonConst.NULL_STRING};
    }
}
