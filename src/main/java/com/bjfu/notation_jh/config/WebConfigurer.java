package com.bjfu.notation_jh.config;

import com.bjfu.notation_jh.handle.JwtInterceptor;
import com.bjfu.notation_jh.handle.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor loginInterceptor;
//    @Value("${web.upload-path}")
//    private String mImagesPath;

    @Resource
    private JwtInterceptor jwtInterceptor;

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/register", "/images/**","/imagesface/**").excludePathPatterns("/static/**").excludePathPatterns("/");
//    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/imagesface/**")
                .excludePathPatterns("/notationImages/**");
    }

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/","file:///profile/imagesface/","file:///profile/notationImages/" };
//            "classpath:/static/", "classpath:/public/","file://G:/000BJFU/imagesface/","file://G:/000BJFU/notationImages/" };


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/imagesface/**").addResourceLocations("file:G:/000BJFU/imagesface/");
        registry.addResourceHandler("/imagesface/**").addResourceLocations("file:/profile/imagesface/");
        registry.addResourceHandler("/notationImages/**").addResourceLocations("file:/profile/notationImages/");
//        registry.addResourceHandler("/notationImages/**").addResourceLocations("file:G:/000BJFU/notationImages/");
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        }
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
        }
        //其他静态资源，与本文关系不大
        registry.addResourceHandler("/upload/**").addResourceLocations("file:upload");
    }

}