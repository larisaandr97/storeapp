package com.javaproject.storeapp.repository;

import com.javaproject.storeapp.entities.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Integer> {
}
