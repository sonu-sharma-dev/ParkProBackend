package com.example.parkingsystembackend.repository;

import com.example.parkingsystembackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
