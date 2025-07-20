package com.example.parkingsystembackend.service;

import com.example.parkingsystembackend.dto.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${STRIPE_PUBLIC_KEY}")
    private String secretKey;


    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public ResponseEntity<?> addCard(String paymentMethodId, String email) {
        Stripe.apiKey = secretKey;

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        try {
            // Create payment method on backend if not provided
            if (paymentMethodId == null || paymentMethodId.isEmpty()) {
                paymentMethodId = "pm_card_visa"; // or other test IDs like pm_card_mastercard
            }

            // Search or create customer
            Customer customer;
            Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("query", "email:'" + email + "'");
            CustomerSearchResult searchResult = Customer.search(searchParams);

            if (!searchResult.getData().isEmpty()) {
                customer = searchResult.getData().get(0);
            } else {
                Map<String, Object> customerParams = new HashMap<>();
                customerParams.put("email", email);
                customer = Customer.create(customerParams);
            }

            // Attach PaymentMethod to Customer
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            Map<String, Object> attachParams = new HashMap<>();
            attachParams.put("customer", customer.getId());
            paymentMethod.attach(attachParams);

            return ResponseEntity.ok("Card successfully added to customer " + customer.getId());

        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Stripe error: " + e.getMessage());
        }
    }



    public ResponseEntity<?> makePayment(String paymentMethodId,String customerId, long amount) {
        Stripe.apiKey = secretKey;
        try {

            paymentMethodId = "pm_card_visa"; // or other test IDs like pm_card_mastercard
            customerId = "cus_SOkFQCdGrZRPau";

            Customer customer = Customer.retrieve(customerId);
            PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setAmount(amount)
                    .setCurrency("usd")
                    .setPaymentMethod(paymentMethodId)
                    .setCustomer(customerId)
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.AUTOMATIC)
                    .setConfirm(true)
                    .build();
            PaymentIntent paymentIntent = PaymentIntent.create(createParams);
            return ResponseEntity.ok().body("Payment successful with payment intent ID: " + paymentIntent.getId());
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    public void charge(PaymentRequest request) throws StripeException {

        Map<String, Object> chargeParams = new HashMap<>();

        chargeParams.put("amount", request.getAmount());
        chargeParams.put("currency", request.getCurrency());
        chargeParams.put("description", request.getDescription());
        chargeParams.put("source", request.getSource());

        Charge.create(chargeParams);
    }
}