package com.goudong.commons.utils;

import com.google.common.collect.Lists;
import com.goudong.commons.annotation.IgnoreResource;
import com.goudong.commons.constant.BasePackageConst;
import com.goudong.commons.constant.CommonConst;
import com.goudong.commons.enumerate.RequestMappingEnum;
import com.goudong.commons.exception.ServerException;
import com.goudong.commons.pojo.ResourceAntMatcher;
import com.goudong.commons.utils.core.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 类描述：
 * 扫描微服务接口(控制器方法)
 * @Author e-Feilong.Chen
 * @Date 2021/8/16 10:37
 */
@Deprecated
public class ResourceUtil {

    /**
     * 扫描的包名，注意：子模块只能扫描到自己的包下的class。（所有微服务的控制层包完全名数组）
     */
    private static final String[] CONTROLLER_PACKAGES = {
            BasePackageConst.USER_CONTROLLER,
            BasePackageConst.OAUTH2_CONTROLLER,
            BasePackageConst.MESSAGE_CONTROLLER,
    };

    /**
     * 生成菜单资源
     * @param contextPath 应用上下文路径
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<ResourceAntMatcher> scanMenu(String contextPath) throws IOException, ClassNotFoundException {
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.generateResourceAntMatchers(contextPath, false);
        // 将路径参数变为*
        resourceAntMatchers.parallelStream().forEach(p->{
            p.setPattern(StringUtil.replacePathVariable2Asterisk(p.getPattern()));
        });
        return resourceAntMatchers;
    }

    /**
     * 生成白名单资源
     * @param contextPath 应用上下文路径
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<ResourceAntMatcher> scanIgnore(String contextPath) throws IOException, ClassNotFoundException {
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.generateResourceAntMatchers(contextPath, true);
        // 将路径参数变为*
        resourceAntMatchers.parallelStream().forEach(p->{
            p.setPattern(StringUtil.replacePathVariable2Asterisk(p.getPattern()));
        });
        return resourceAntMatchers;
    }

    /**
     * 扫描所有接口
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<ResourceAntMatcher> generateResourceAntMatchers(String contextPath, boolean isIgnoreResource) throws IOException, ClassNotFoundException {
        List<Resource> resources = ResourceUtil.generateClassResources();

        List<ResourceAntMatcher> resourceAntMatchers = new ArrayList<>();

        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(new PathMatchingResourcePatternResolver());
        // 循环资源
        for (Resource resource : resources) {
            // 资源可以读取
            if (resource.isReadable()) {
                // 元数据输入流（输入字节流）
                MetadataReader reader = readerFactory.getMetadataReader(resource);

                // 获取class完全限定名
                String className = reader.getClassMetadata().getClassName();
                // 获取Class对象
                Class<?> clazz = Class.forName(className);
                // 获取类上的@RequestMapping注解
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                // 有注解表明该类是控制器。
                if (requestMapping != null) {
                    // 获取控制器中用户自定义的方法数组
                    Method[] methods = clazz.getDeclaredMethods();
                    // 将服务的统一前缀拼接进控制器请求地址
                    String[] prefixArr = ResourceUtil.combinationPath(contextPath, requestMapping.value());
                    // 循环方法
                    for (Method method: methods) {
                        // 获取方法上的 @XXXMapping注解
                        Annotation httpMethodAnnotation = ResourceUtil.getHttpMethodAnnotation(method);
                        // 方法有@XXXMapping注解，表明是一个接口
                        if (httpMethodAnnotation != null) {
                            // 接口备注（白名单备注|菜单备注）。
                            String remark = "api";
                            // 当是获取白名单资源时
                            if (isIgnoreResource) {
                                IgnoreResource ignoreResource = method.getAnnotation(IgnoreResource.class);
                                if (ignoreResource != null) {
                                    remark = ignoreResource.value();
                                    resourceAntMatchers.addAll(
                                            ResourceUtil.annotation2ResourceAntMatcher(httpMethodAnnotation, prefixArr, remark)
                                    );
                                }

                                // 跳过本次循环
                                continue;
                            }

                            // 默认是获取菜单资源
                            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                            if (apiOperation != null) {
                                remark = apiOperation.value();
                            }

                            resourceAntMatchers.addAll(
                                    ResourceUtil.annotation2ResourceAntMatcher(httpMethodAnnotation, prefixArr, remark)
                            );
                        }
                    }
                }

            }
        }

        return resourceAntMatchers;
    }
    /**
     * 生产class的resource资源集合，用于后续扫描资源
     * @return
     */
    private static List<Resource> generateClassResources() {
        // 类路径下的位置
        final String classPattern = CommonConst.ALL_CLASSES_ANT_PATH;

        List<Resource> resources = new ArrayList<>();
        Stream.of(ResourceUtil.CONTROLLER_PACKAGES).forEach(p->{
            String pattern = new StringBuilder()
                    .append(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)
                    .append(ClassUtils.convertClassNameToResourcePath(p))
                    .append(classPattern).toString();
            try {
                resources.addAll(Lists.newArrayList(new PathMatchingResourcePatternResolver().getResources(pattern)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return resources;
    }
    /**
     * 获取api接口的请求方式
     * @param method
     * @return
     */
    public static Annotation getHttpMethodAnnotation(Method method){
        // 接口请求Mapping，自定义的枚举
        RequestMappingEnum[] values = RequestMappingEnum.values();
        for (int i = 0; i < values.length; i++) {
            Annotation annotation = method.getAnnotation(values[i].getMapping());
            // 方法是一个web接口
            if (annotation != null) {
                return annotation;
            }
        }

        // 普通方法
        return null;
    }

    /**
     * 将接口请求注解转换成 白名单数据
     * @param annotation 注解（值可能是 @Requestmapping、@Getmapping、@PostMapping、@DeleteMapping、@PutMapping等）
     * @param prefixArr 控制器类上的注解，请求前缀
     * @param remark 备注
     * @return
     */
    private static List<ResourceAntMatcher> annotation2ResourceAntMatcher (Annotation annotation, String[] prefixArr, String remark) {
        if (annotation instanceof RequestMapping) {
            RequestMapping requestMapping = (RequestMapping) annotation;
            // 请求方式
            RequestMethod[] methods = requestMapping.method();
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(remark, methods, path);
        }

        if (annotation instanceof GetMapping) {
            GetMapping requestMapping = (GetMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.GET};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(remark, methods, path);
        }

        if (annotation instanceof PostMapping) {
            PostMapping requestMapping = (PostMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.POST};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(remark, methods, path);
        }

        if (annotation instanceof PutMapping) {
            PutMapping requestMapping = (PutMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.PUT};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(remark, methods, path);
        }

        if (annotation instanceof DeleteMapping) {
            DeleteMapping requestMapping = (DeleteMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.DELETE};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(remark, methods, path);
        }

        if (annotation instanceof PatchMapping) {
            PatchMapping requestMapping = (PatchMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.PATCH};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(remark, methods, path);
        }

        String errorMessage = StringUtil.format("注解{}不是有效的http接口注解",annotation);
        throw ServerException.serverException(errorMessage);
    }

    /**
     * 根据请求路径和接口请求method数据生成ResourceAntMatcher集合
     * @param remark 接口备注
     * @param methods 接口请求方式数组
     * @param path 接口请求路径数组
     * @return
     */
    private static List<ResourceAntMatcher> addResourceAntMatcher(String remark, RequestMethod[] methods, String[] path) {
        List<ResourceAntMatcher> var0 = new ArrayList<>();
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < methods.length; j++) {
                var0.add(new ResourceAntMatcher(path[i], methods[j].name(), remark));
            }
        }
        return var0;
    }

    /**
     * 组合接口地址，加上应用上下文路径前缀，如果paths为空数组（@GetMapping）没有定义value和path，那么就给一个默认值/
     * @param contextPath 上下文请求路径
     * @param paths 请求地址数组
     * @return
     */
    private static String[] combinationPath(String contextPath, String[] paths) {
        if (paths.length > 0) {
            if (StringUtils.hasText(contextPath)) {
                // 添加前缀
                for (int i = 0; i < paths.length; i++) {
                    paths[i] = contextPath + paths[i];
                }

                return paths;
            }

            return paths;
        }

        contextPath = Optional.ofNullable(contextPath).orElseGet(()->"");

        return new String[]{contextPath + CommonConst.NULL_STRING};
    }

    /**
     * 组合 前缀数组和请求地址数组 如果paths为空数组（@GetMapping）没有定义value和path，那么就给一个默认值/
     * @param prefixs 前缀数组
     * @param paths 请求地址数组
     * @return
     */
    private static String[] combinationPath(String[] prefixs, String[] paths) {
        List<String> result = new ArrayList<>();
        // 没有定义path，那么就给个默认值
        if (paths.length == 0) {
            paths = new String[]{CommonConst.NULL_STRING};
        }

        for (int i = 0; i < prefixs.length; i++) {
            for (int j = 0; j < paths.length; j++) {
                result.add(prefixs[i] + paths[j]);
            }
        }

        return result.toArray(new String[result.size()]);
    }

}
