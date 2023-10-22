package com.example.library.repository.library;

import com.example.library.model.library.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
}
