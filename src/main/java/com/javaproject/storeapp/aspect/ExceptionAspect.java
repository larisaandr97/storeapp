package com.javaproject.storeapp.aspect;

import com.javaproject.storeapp.entity.Audit;
import com.javaproject.storeapp.entity.AuditCategory;
import com.javaproject.storeapp.repository.AuditRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class ExceptionAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuditRepository auditRepository;

    public ExceptionAspect(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @AfterThrowing("execution(* com.javaproject.storeapp.service.impl.OrderServiceImpl.findOrderById(..))")
    public void afterThrowingFindOrderById() {
        final Audit audit = new Audit("findOrderById",
                this.getClass().getCanonicalName(),
                LocalDateTime.now(), AuditCategory.ERROR);
        auditRepository.save(audit);
        this.logger.info("An exception was thrown inside findOrderById from OrderService ");
    }

    @AfterThrowing("execution(* com.javaproject.storeapp.service.impl.BankAccountServiceImpl.createBankAccount(..))")
    public void afterThrowing() {

        final Audit audit = new Audit("createBankAccount",
                this.getClass().getCanonicalName(),
                LocalDateTime.now(), AuditCategory.ERROR);
        auditRepository.save(audit);
        this.logger.info("A bank account with this number was already added! ");
    }

    @Around("execution(* com.javaproject.storeapp.service.impl.OrderServiceImpl.checkBalanceForOrder(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        this.logger.info("start execution");
        Object result = null;
        try {
            result = joinPoint.proceed();
            final Audit audit = new Audit(joinPoint.getSignature().getName(),
                    joinPoint.getTarget().getClass().getCanonicalName(),
                    LocalDateTime.now(), AuditCategory.INFO);
            auditRepository.save(audit);
            this.logger.info("execution proceeded");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
