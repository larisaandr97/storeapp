package com.javaproject.storeapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class TracingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Map<String, Integer> withdrawCount = new HashMap<>();

    @Pointcut("execution (private * com.javaproject.storeapp.controller.BankAccountController.*(..))")
    public void privateBankControllerMethod() {
    }

    @Pointcut("execution (public * com.javaproject.storeapp.service.OrderService.*(..))")
    public void allPublicOrderServiceMethods() {
    }

    @Around("@annotation(com.javaproject.storeapp.annotations.TrackExecutionTime)")
    public Object executionTime(final ProceedingJoinPoint point) throws Throwable {
        final long startTime = System.currentTimeMillis();
        final Object object = point.proceed();
        final long endTime = System.currentTimeMillis();
        logger.info("Class Name: " + point.getSignature().getDeclaringTypeName() + ". Method Name: " + point.getSignature().getName() + ". Time taken for Execution is : " + (endTime - startTime) + "ms");
        return object;
    }

    @Before("allPublicOrderServiceMethods()")
    public void tracingServiceMethods(final JoinPoint joinPoint) {
        final Signature signature = joinPoint.getSignature();
        logger.info("Entering [" + signature.toString() + "]");
    }

    @After("execution ( * com.javaproject.storeapp.service.BankAccountService.withdrawMoneyFromAccount(..))")
    public void countNumberOfWithdraws(final JoinPoint joinPoint) {
        final String methodName = joinPoint.getSignature().getName();
        final Optional<Object> accountIdOptional = Arrays.stream(joinPoint.getArgs()).findFirst();
        if (accountIdOptional.isPresent()) {
            final String accountId = accountIdOptional.get().toString();
            if (withdrawCount.containsKey(accountId)) {
                withdrawCount.put(accountId, withdrawCount.get(accountId) + 1);
            } else {
                withdrawCount.put(accountId, 1);
            }
            logger.info("Called " + methodName + "for a total of " + withdrawCount.get(accountId) + " times for account with Id " + accountId);
        }
    }

    @After("privateBankControllerMethod()")
    public void tracingPrivateBankMethod(final JoinPoint joinPoint) {
        final Signature signature = joinPoint.getSignature();
        logger.info("Entering private method [" + signature.toString() + "]");
    }
}