package com.javaproject.storeapp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterThrowing("execution(* com.javaproject.storeapp.service.impl.OrderServiceImpl.findOrderById(..))")
    public void afterThrowingFindOrderById() {
        this.logger.info("An exception was thrown inside findOrderById from OrderService ");
    }

    @AfterThrowing("execution(* com.javaproject.storeapp.service.impl.BankAccountServiceImpl.createBankAccount(..))")
    public void afterThrowing() {
        this.logger.info("A bank account with this number was already added! ");
    }

    @Around("execution(* com.javaproject.storeapp.service.impl.OrderServiceImpl.checkBalanceForOrder(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        this.logger.info("start execution");
        Object result = null;
        try {
            result = joinPoint.proceed();
            this.logger.info("execution proceeded");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
