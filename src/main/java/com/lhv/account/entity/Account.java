package com.lhv.account.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phoneNr;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDtime;

    @Column(nullable = false)
    private LocalDateTime modifiedDtime;

    @PrePersist
    protected void onCreate() {
        createdDtime = LocalDateTime.now();
        modifiedDtime = createdDtime;
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDtime = LocalDateTime.now();
    }
}

