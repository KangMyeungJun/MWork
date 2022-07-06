package com.mwork.main.config;

import com.mwork.main.home.interceptor.CommentInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CommentInterceptor())
                .addPathPatterns("/post/*/comment")
                .addPathPatterns("/post/*/comment/**");
    }
}
