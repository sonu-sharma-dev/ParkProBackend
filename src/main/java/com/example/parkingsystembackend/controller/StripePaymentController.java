package com.example.parkingsystembackend.controller;

import com.example.parkingsystembackend.dto.PaymentRequest;
import com.example.parkingsystembackend.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/stripe/payment")
@RequiredArgsConstructor
public class StripePaymentController {

    private final StripeService stripeService;


    @PostMapping("/add")
    public ResponseEntity<?> addCard(@RequestBody Map<String, String> payload) {
        String paymentMethodId = payload.get("paymentMethodId");
        String email = payload.get("email");
        return stripeService.addCard(paymentMethodId, email);
    }

    @PostMapping("/charge")
    public ResponseEntity<?> chargeCard(@RequestBody PaymentRequest request) {
        try {
            stripeService.charge(request);
            return ResponseEntity.ok().body("Payment Successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/make-payment")
    public ResponseEntity<?> makePayment(
            @RequestParam String paymentMethodId,
            @RequestParam int amount,
            @RequestParam String customerId) {

        return stripeService.makePayment(paymentMethodId, customerId, amount);
    }
}