package com.javaproject.storeapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = {"com.javaproject.storeapp.service","com.javaproject.storeapp.entity", "com.javaproject.storeapp.aspect","com.javaproject.storeapp.service.impl","com.javaproject.storeapp.controller","org.springframework.web.bind.annotation.RequestMapping"})
@EnableAspectJAutoProxy
public class AspectConfig {
}
