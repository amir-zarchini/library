package com.example.library.controller.library;

import com.example.library.model.library.Checkout;
import com.example.library.service.library.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/library/checkout/{bookId}/{membershipId}")
    public ResponseEntity<Checkout> checkout(@PathVariable("bookId") Long bookId,
                                             @PathVariable("membershipId") Long membershipId) {
        return service.checkout(bookId, membershipId);
    }
}
