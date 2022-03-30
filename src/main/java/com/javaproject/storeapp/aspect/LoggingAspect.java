package com.javaproject.storeapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);



    @After("execution(* com.javaproject.storeapp.controller.LoginController.loginForm(..))")
    public void logControllerMethod(JoinPoint joinPoint) {

        logger.info("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " will be executed. Timestamp: " + LocalDateTime.now());


    }

    @After("execution(* com.javaproject.storeapp.controller.ProductController.addProduct(..))")
    public void logServiceMethod(JoinPoint joinPoint) {

        logger.info("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " will be executed. Timestamp: " + LocalDateTime.now());



    }



}
