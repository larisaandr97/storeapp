package com.javaproject.storeapp.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String signature;

    @Column(name = "class")
    private String targetClass;

    @Column(name = "timestamp_value")
    private LocalDateTime timestampValue;

    public Audit(String signature, String targetClass, LocalDateTime timestampValue) {
        this.signature = signature;
        this.targetClass = targetClass;
        this.timestampValue = timestampValue;
    }

    public Audit() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public LocalDateTime getTimestampValue() {
        return timestampValue;
    }

    public void setTimestampValue(LocalDateTime timestampValue) {
        this.timestampValue = timestampValue;
    }
}
