package com.example.library.model.library;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "membership")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String membershipNumber;

    @Column(unique=true)
    private String nationalId;

    private String firstName;

    private String lastName;

    private Date bornDate;

    private Date membershipDate;

    private String fatherName;

    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "membership_books",
            joinColumns = { @JoinColumn(name = "membership_id") },
            inverseJoinColumns = { @JoinColumn(name = "book_id") })
    private Set<Book> books = new HashSet<>();
}
