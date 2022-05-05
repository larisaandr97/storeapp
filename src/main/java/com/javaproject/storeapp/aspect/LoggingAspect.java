package com.javaproject.storeapp.aspect;

import com.javaproject.storeapp.entity.Audit;
import com.javaproject.storeapp.entity.AuditCategory;
import com.javaproject.storeapp.repository.AuditRepository;
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
    private final AuditRepository auditRepository;

    public LoggingAspect(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @AfterReturning("execution(* com.javaproject.storeapp.controller.LoginController.*(..))")
    public void logLoginControllerMethod(JoinPoint joinPoint) {
        final Audit audit = new Audit(joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getCanonicalName(),
                LocalDateTime.now(), AuditCategory.INFO);
        auditRepository.save(audit);

        logger.info("Method " + joinPoint.getSignature().toString() +
                " from " + joinPoint.getTarget().getClass() +
                " will be executed. Timestamp: " + LocalDateTime.now());
    }

    @After("execution(* com.javaproject.storeapp.controller.ProductController.addProduct(..))")
    public void logProductServiceMethod(JoinPoint joinPoint) {
        final Audit audit = new Audit(joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getCanonicalName(),
                LocalDateTime.now(), AuditCategory.INFO);
        auditRepository.save(audit);

        logger.info("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " will be executed. Timestamp: " + LocalDateTime.now());

    }

    @After("execution(* com.javaproject.storeapp.controller.CartController.addProductToCart(..))")
    public void logCartControllerMethod(JoinPoint joinPoint) {
        final Audit audit = new Audit(joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getCanonicalName(),
                LocalDateTime.now(), AuditCategory.INFO);
        auditRepository.save(audit);

        logger.info("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " will be executed. Timestamp: " + LocalDateTime.now());
    }

    @AfterReturning("execution(* com.javaproject.storeapp.controller.CartController.checkout(..))")
    public void logCheckoutMethod(JoinPoint joinPoint) {
        final Audit audit = new Audit(joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getCanonicalName(),
                LocalDateTime.now(), AuditCategory.INFO);
        auditRepository.save(audit);

        logger.info("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " will be executed. Timestamp: " + LocalDateTime.now());
    }

}
