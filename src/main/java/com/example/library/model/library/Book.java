package com.example.library.model.library;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue
    @Column(length = 16)
    private Long id;

    private String title;

    private String isbn;

    private Date publishDate;

    private String authors;

    private String translator;

    private String publishYear;

    private String edition;

    private Integer count;
}
