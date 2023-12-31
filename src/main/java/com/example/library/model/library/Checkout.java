package com.example.library.model.library;

import com.example.library.model.security.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "checkout")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Checkout {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private Membership membership;

    private Date checkoutDate;

    private Date expirationDate;

    private Boolean isReserved;
}
