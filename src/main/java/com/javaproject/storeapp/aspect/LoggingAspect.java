package com.javaproject.storeapp.aspect;

import com.javaproject.storeapp.entities.Audit;
import com.javaproject.storeapp.repository.AuditRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private AuditRepository auditRepository;

    @AfterReturning("execution(* com.javaproject.storeapp.service.*.*(..))")
    public void log(JoinPoint joinPoint) {
        System.out.println("Method " + joinPoint.getSignature().getName() +
                " from " + joinPoint.getTarget().getClass() +
                " will be executed. Timestamp: " + LocalDateTime.now());

        Audit audit = new Audit(joinPoint.getSignature().toString(), joinPoint.getTarget().getClass().getCanonicalName(), LocalDateTime.now());
        auditRepository.save(audit);
    }

}
