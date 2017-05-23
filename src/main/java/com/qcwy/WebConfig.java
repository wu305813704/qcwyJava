package com.qcwy;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置外部静态资源映射路径
 *
 * Created by KouKi on 2017/3/7.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    //本地文件映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/image/**").addResourceLocations("file:D:/qcwy/image/");//windows
        registry.addResourceHandler("/image/**").addResourceLocations("file:/qcwy/image/");//Linux
        super.addResourceHandlers(registry);
    }

//    //拦截器注册
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**")/** 需要拦截的请求*/.excludePathPatterns()/** 派出拦截的请求*/;
//        super.addInterceptors(registry);
//    }
}
