package com.example.parkingsystembackend.controller;

import com.example.parkingsystembackend.entity.Payment;
import com.example.parkingsystembackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/getAllPayment")
    public List<Payment>getAllPayment(){
        return paymentService.getAllPayment();
    }

}
