package com.javaproject.storeapp.aspect;

import com.javaproject.storeapp.exception.ResourceNotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public final class DataValidationAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.javaproject.storeapp.service.ProductService.createProduct(..))")
    public void createProductMethod() {
    }

    @Pointcut("execution(* com.javaproject.storeapp.service.ProductService.findProductById(..))")
    public void findProductMethod() {
    }

    @Around("findProductMethod()")
    public Object validateFindProductByIdMethod(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final Object ret = proceedingJoinPoint.proceed();
        final String productId = String.valueOf(Arrays.stream(proceedingJoinPoint.getArgs()).findFirst().get());
        if (ret == null) {
            logger.error("When calling method " + proceedingJoinPoint.getSignature() + " no product with Id " + productId + " was found.");
            throw new ResourceNotFoundException("Product with Id " + productId + " not found.");
        } else {
            logger.info("Found product class:" + ret.getClass().getSimpleName() + " and Id = " + productId);
            return ret;
        }
    }

    @AfterReturning(
            pointcut = "createProductMethod()",
            returning = "result")
    public void logAfterReturning(final JoinPoint joinPoint, final Object result) {
        logger.info("Created new product in method " + joinPoint.getSignature().getName());
        logger.info("Method returned value of product: " + result);
    }

}
