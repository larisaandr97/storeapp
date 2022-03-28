package com.javaproject.storeapp.aspect;

import com.javaproject.storeapp.entity.Audit;
import com.javaproject.storeapp.repository.AuditRepository;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public aspect World {

    pointcut greeting(): execution (* com.javaproject.storeapp.service.ProductService.createProduct());

    after() returning(): greeting() {
        System.out.println("Hello!");
    }
}