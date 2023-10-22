package com.example.library.service.library;

import com.example.library.model.library.Checkout;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CheckoutService {
    public ResponseEntity<Checkout> checkout(UUID bookId, Long userId) {
        if (bookId == null || userId == null) {
            throw new NullPointerException("خطا در پارامترهای ورودی");
        }
        return null;
    }
}
