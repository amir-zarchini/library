package com.example.library.model.library;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
}
