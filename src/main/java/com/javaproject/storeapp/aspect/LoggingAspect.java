package com.javaproject.storeapp.aspect;

import com.javaproject.storeapp.entity.Audit;
import com.javaproject.storeapp.repository.AuditRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


//    @After("execution(* com.javaproject.storeapp.service.*.*(..))")
//    public void log(JoinPoint joinPoint) {
////        log.info("Method " + joinPoint.getSignature().getName() +
////                " from " + joinPoint.getTarget().getClass() +
////                " will be executed. Timestamp: " + LocalDateTime.now());
////
////        Audit audit = new Audit(joinPoint.getSignature().toString(), joinPoint.getTarget().getClass().getCanonicalName(), LocalDateTime.now());
////        auditRepository.save(audit);
//        System.out.println("Da da da");
//    }
    @After("execution(* com.javaproject.storeapp.controller.LoginController.loginForm(..))")
    public void logController(JoinPoint joinPoint) {
//        log.info("Method " + joinPoint.getSignature().getName() +
//                " from " + joinPoint.getTarget().getClass() +
//                " will be executed. Timestamp: " + LocalDateTime.now());
//
//        Audit audit = new Audit(joinPoint.getSignature().toString(), joinPoint.getTarget().getClass().getCanonicalName(), LocalDateTime.now());
//        auditRepository.save(audit);
        System.out.println("Da da da2321");
    }

}
