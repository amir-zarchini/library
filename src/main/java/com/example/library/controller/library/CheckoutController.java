package com.example.library.controller.library;

import com.example.library.model.library.Checkout;
import com.example.library.service.library.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/library")
public class CheckoutController {

    private final CheckoutService service;


    @PostMapping("/checkout/{bookId}/{membershipId}")
    public ResponseEntity<Checkout> checkout(@PathVariable("bookId") UUID bookId,
                                             @PathVariable("membershipId") UUID membershipId) {
        return service.checkout(bookId, membershipId);
    }
}
