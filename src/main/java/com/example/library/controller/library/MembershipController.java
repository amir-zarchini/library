package com.example.library.controller.library;

import com.example.library.model.library.Membership;
import com.example.library.payload.response.MessageResponse;
import com.example.library.repository.library.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipRepository membershipRepository;


    @GetMapping("/api/library/membership/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Membership>> getAllMemberships() {
        return ResponseEntity.ok(membershipRepository.findAll());
    }

    @GetMapping("/api/library/membership/find/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<Membership>> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(membershipRepository.findById(id));
    }

    @PostMapping("/api/library/membership/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Membership> createMembership(@RequestBody Membership Membership) {
        return ResponseEntity.ok(membershipRepository.save(Membership));
    }

    @PutMapping("/api/library/membership/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Membership> updateMembership(@RequestBody Membership Membership) {
        return ResponseEntity.ok(membershipRepository.save(Membership));
    }

    @DeleteMapping("/api/library/membership/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMembership(@PathVariable("id") Long id) {
        try{
            membershipRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("delete successful!"));
        }
        catch (Exception ignored) {
            throw new RuntimeException("error in delete field!");
        }
    }
}
