package com.javaproject.storeapp.aspect;

import com.javaproject.storeapp.entity.Audit;
import com.javaproject.storeapp.entity.AuditCategory;
import com.javaproject.storeapp.exception.ResourceNotFoundException;
import com.javaproject.storeapp.repository.AuditRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public final class DataValidationAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuditRepository auditRepository;

    public DataValidationAspect(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

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
            final Audit audit = new Audit(proceedingJoinPoint.getSignature().getName(),
                    proceedingJoinPoint.getTarget().getClass().getCanonicalName(),
                    LocalDateTime.now(), AuditCategory.ERROR);
            auditRepository.save(audit);
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
        final Audit audit = new Audit(joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getCanonicalName(),
                LocalDateTime.now(), AuditCategory.INFO);
        auditRepository.save(audit);
        logger.info("Created new product in method " + joinPoint.getSignature().getName());
        logger.info("Method returned value of product: " + result);
    }

}
