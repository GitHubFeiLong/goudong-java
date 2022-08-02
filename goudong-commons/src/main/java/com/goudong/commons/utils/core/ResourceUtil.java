package com.goudong.commons.utils.core;

import com.google.common.collect.Lists;
import com.goudong.commons.annotation.core.Inner;
import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.constant.core.BasePackageConst;
import com.goudong.commons.constant.core.CommonConst;
import com.goudong.commons.constant.core.HttpMethodConst;
import com.goudong.commons.enumerate.core.RequestMappingEnum;
import com.goudong.commons.exception.ServerException;
import com.goudong.commons.framework.core.ResourceAntMatcher;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.annotation.AnnotationUtils;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 * 扫描微服务接口(控制器方法)
 * @Author e-Feilong.Chen
 * @Date 2021/8/16 10:37
 */
public class ResourceUtil {

    /**
     * 扫描的包名，注意：子模块只能扫描到自己的包下的class。（所有微服务的控制层包完全名数组）
     */
    private static final String[] CONTROLLER_PACKAGES = {
            BasePackageConst.USER_CONTROLLER,
            BasePackageConst.OAUTH2_CONTROLLER,
            BasePackageConst.MESSAGE_CONTROLLER,
            BasePackageConst.FILE_CONTROLLER,
            BasePackageConst.BPM_CONTROLLER,
    };

    /**
     * 是否是有效的方法
     * @param methods
     * @return
     */
    public static boolean validMethods(String methods) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(methods)) {
            // 转大写，使用逗号进行分割变为list集合
            List<String> httpMethods = Stream.of(methods.toUpperCase().split(",")).collect(Collectors.toList());
            return ResourceUtil.validMethods(httpMethods);
        }
        return false;
    }
    /**
     * 是否是有效的方法
     * @param methods http请求方法
     * @return
     */
    public static boolean validMethods(List<String> methods) {
        if (CollectionUtils.isNotEmpty(methods)) {
            // 等于0表示都在指定范围内
            return HttpMethodConst.ALL_HTTP_METHOD.containsAll(methods);
        }

        return false;
    }


     /**
      * 扫描api接口资源
      *
      * @param contextPath 应用上下文路径
      * @return
      * @throws IOException
      * @throws ClassNotFoundException
      */
     public static List<ResourceAntMatcher> scanApiResource(String contextPath) throws IOException, ClassNotFoundException {
         List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanResourceByType(contextPath, ScanTypeEnum.API_RESOURCE);
         return resourceAntMatchers;
     }

    /**
     * 扫描白名单
     *
     * @param contextPath 应用上下文路径
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<ResourceAntMatcher> scanWhitelist(String contextPath) throws IOException, ClassNotFoundException {
        return ResourceUtil.scanResourceByType(contextPath, ScanTypeEnum.WHITELIST);
    }

    /**
     * 根据类型，扫描资源接口
     * @param contextPath 服务设置的上下文路径
     * @param scanTypeEnum 扫描类型
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<ResourceAntMatcher> scanResourceByType (String contextPath, ScanTypeEnum scanTypeEnum)
            throws IOException, ClassNotFoundException {

        // 获取类路径下的资源
        List<Resource> resources = ResourceUtil.generateClassResources();

        // 结果容器
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

                            switch (scanTypeEnum) {
                                // 当是获取白名单资源时
                                case WHITELIST: {
                                    //Whitelist whitelist = method.getAnnotation(Whitelist.class);
                                    Whitelist whitelist = AnnotationUtils.findAnnotation(method, Whitelist.class);

                                    if (whitelist != null) {
                                        // 备注
                                        remark = whitelist.value();
                                        // 关闭该接口
                                        boolean disable = whitelist.disable();

                                        // 默认不是只能内部服务调用
                                        boolean isInner = false;
                                        // 判断是否是Inner注解，若是，代表接口是内部服务调用白名单（功能：内部调用不鉴权，外部服务调用鉴权）
                                        Inner inner = AnnotationUtils.findAnnotation(method, Inner.class);
                                        if (inner != null) {
                                            remark = inner.value();
                                            isInner = true;
                                            disable = inner.disable();
                                        }

                                        resourceAntMatchers.addAll(
                                                ResourceUtil.annotation2ResourceAntMatcherByWhitelist(httpMethodAnnotation, prefixArr, remark, isInner, disable)
                                        );
                                    }

                                    break;
                                }
                                // 当是apiResource资源时,只要是http接口，都进行获取处理
                                case API_RESOURCE: {
                                    resourceAntMatchers.addAll(
                                            ResourceUtil.annotation2ResourceAntMatcherByApiResource(httpMethodAnnotation, prefixArr)
                                    );
                                }

                            }

                        }
                    }
                }

            }
        }

        // 将路径参数变为*
        resourceAntMatchers.parallelStream().forEach(p->{
            p.setPattern(StringUtil.replacePathVariable2Asterisk(p.getPattern()));
        });

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
            // 获取方法上的Restful的请求注解
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
     * @param isInner 是否只能内部服务调用
     * @param disable 是否关闭该白名单
     * @return
     */
    private static List<ResourceAntMatcher> annotation2ResourceAntMatcherByWhitelist (Annotation annotation,
                                                                           String[] prefixArr,
                                                                           String remark,
                                                                           boolean isInner,
                                                                           boolean disable) {
        return annotation2ResourceAntMatcher(ScanTypeEnum.WHITELIST, annotation, prefixArr, remark, isInner, disable);
    }

    /**
     * 将接口请求注解转换成 apiResource数据
     * @param annotation 注解（值可能是 @Requestmapping、@Getmapping、@PostMapping、@DeleteMapping、@PutMapping等）
     * @param prefixArr 控制器类上的注解，请求前缀
     * @return
     */
    private static List<ResourceAntMatcher> annotation2ResourceAntMatcherByApiResource (Annotation annotation,
                                                                                      String[] prefixArr) {
        return annotation2ResourceAntMatcher(ScanTypeEnum.API_RESOURCE, annotation, prefixArr, null, false, false);
    }

    /**
     * 将接口请求注解转换成 基础数据，共其他方使用
     * @param scanTypeEnum 类型，类型不同生成对象的结果不同
     * @param annotation 注解（值可能是 @Requestmapping、@Getmapping、@PostMapping、@DeleteMapping、@PutMapping等）
     * @param prefixArr 控制器类上的注解，请求前缀
     * @param remark 备注
     * @param isInner 是否只能内部服务调用
     * @param disable 是否关闭该白名单
     * @return
     */
    private static List<ResourceAntMatcher> annotation2ResourceAntMatcher (ScanTypeEnum scanTypeEnum,
                                                                           Annotation annotation,
                                                                           String[] prefixArr,
                                                                           String remark,
                                                                           boolean isInner,
                                                                           boolean disable) {
        if (annotation instanceof RequestMapping) {
            RequestMapping requestMapping = (RequestMapping) annotation;
            // 请求方式
            RequestMethod[] methods = requestMapping.method();
            if (methods.length == 0) {
                methods = RequestMethod.values();
            }
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(scanTypeEnum, remark, methods, path, isInner, disable);
        }

        if (annotation instanceof GetMapping) {
            GetMapping requestMapping = (GetMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.GET};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(scanTypeEnum, remark, methods, path, isInner, disable);
        }

        if (annotation instanceof PostMapping) {
            PostMapping requestMapping = (PostMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.POST};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(scanTypeEnum, remark, methods, path, isInner, disable);
        }

        if (annotation instanceof PutMapping) {
            PutMapping requestMapping = (PutMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.PUT};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(scanTypeEnum, remark, methods, path, isInner, disable);
        }

        if (annotation instanceof DeleteMapping) {
            DeleteMapping requestMapping = (DeleteMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.DELETE};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(scanTypeEnum, remark, methods, path, isInner, disable);
        }

        if (annotation instanceof PatchMapping) {
            PatchMapping requestMapping = (PatchMapping) annotation;
            // 请求方式
            RequestMethod[] methods = new RequestMethod[]{RequestMethod.PATCH};
            // 请求地址
            String[] value = requestMapping.value();
            // 请求路径
            String[] path = combinationPath(prefixArr, value);

            return addResourceAntMatcher(scanTypeEnum, remark, methods, path, isInner, disable);
        }

        String errorMessage = StringUtil.format("注解{}不是有效的http接口注解",annotation);
        throw ServerException.serverException(errorMessage);
    }

    /**
     * 根据请求路径和接口请求method数据生成ResourceAntMatcher集合
     * @param scanTypeEnum 类型
     * @param remark 接口备注
     * @param methods 接口请求方式数组
     * @param path 接口请求路径数组
     * @param isInner 是否只能内部服务调用
     * @param disable 是否关闭该白名单
     * @return
     */
    private static List<ResourceAntMatcher> addResourceAntMatcher(ScanTypeEnum scanTypeEnum, String remark, RequestMethod[] methods, String[] path, boolean isInner, boolean disable) {
        final List<ResourceAntMatcher> var0 = new ArrayList<>();

        switch (scanTypeEnum) {
            // 白名单，方法存数组
            case WHITELIST:
                List<String> httpMethods = Stream.of(methods).map(RequestMethod::name).collect(Collectors.toList());
                for (int i = 0; i < path.length; i++) {
                    var0.add(ResourceAntMatcher.createByWhitelist(path[i], httpMethods, remark, isInner, disable));
                }
                break;
            // api 资源，将一个pattern多个method的接口保存多条记录
            case API_RESOURCE:
                for (int i = 0; i < path.length; i++) {
                    for (int j = 0; j < methods.length; j++) {
                        var0.add(ResourceAntMatcher.createByApiResource(path[i], methods[j].name()));
                    }
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

    /**
     * 类描述：
     * 扫描类型枚举
     * @author cfl
     * @date 2022/8/3 1:00
     * @version 1.0
     */
    enum ScanTypeEnum {
        /**
         * 白名单
         */
        WHITELIST,

        /**
         * api接口资源
         */
        API_RESOURCE,
        ;
    }
}
