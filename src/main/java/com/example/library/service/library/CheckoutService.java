package com.example.library.service.library;

import com.example.library.model.library.Book;
import com.example.library.model.library.Checkout;
import com.example.library.model.library.Membership;
import com.example.library.repository.library.BookRepository;
import com.example.library.repository.library.CheckoutRepository;
import com.example.library.repository.library.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContextException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final BookRepository bookRepository;
    private final MembershipRepository membershipRepository;
    public ResponseEntity<Checkout> checkout(UUID bookId, UUID membershipId) {
        checkValidation(bookId, membershipId);
        Checkout checkout = createCheckout();
        try {
            return ResponseEntity.ok(checkoutRepository.save(checkout));
        } catch (Exception e) {
            throw new ApplicationContextException("خطا در save درخواست");
        }
    }

    private Checkout createCheckout() {
        Checkout checkout = new Checkout();
        checkout.setCheckoutDate(new Date());
        checkout.setExpirationDate(new Date(checkout.getCheckoutDate().getTime()
                + TimeUnit.DAYS.toDays(14)));
        return checkout;
    }

    private void checkValidation(UUID bookId, UUID membershipId) {
        if (bookId == null || membershipId == null) {
            throw new NullPointerException("خطا در پارامترهای ورودی");
        }
        Optional<Book> book = Optional.ofNullable(bookRepository.findById(bookId).orElseThrow(
                () -> new DataAccessResourceFailureException("خطا در فراخوانی اطلاعات book")));
        Optional<Membership> membership = Optional.ofNullable(membershipRepository.findById(membershipId).orElseThrow(
                () -> new DataAccessResourceFailureException("خطا در فراخوانی اطلاعات membership")
        ));
        if (book.isEmpty() || membership.isEmpty()){
            throw new NullPointerException("موردی یافت نشد");
        }
    }
}
