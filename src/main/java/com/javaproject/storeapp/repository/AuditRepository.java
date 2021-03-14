package com.javaproject.storeapp.repository;

import com.javaproject.storeapp.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Integer> {
}
