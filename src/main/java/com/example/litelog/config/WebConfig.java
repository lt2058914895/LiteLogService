package com.example.litelog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${avatar.upload.path}")
    private String avatarUploadPath;

    @Value("${avatar.base.url}")
    private String avatarBaseUrl;

    @Value("${record.image.upload.path:./uploads/records}")
    private String recordImageUploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射头像访问路径
        registry.addResourceHandler("/api/avatars/**")
                .addResourceLocations("file:" + avatarUploadPath + "/");
        
        // 映射记录图片访问路径
        registry.addResourceHandler("/api/record-images/**")
                .addResourceLocations("file:" + recordImageUploadPath + "/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}