package com.example.library.service.security;

import com.example.library.exception.RefreshTokenException;
import com.example.library.model.security.ERole;
import com.example.library.model.security.RefreshToken;
import com.example.library.model.security.Role;
import com.example.library.model.security.User;
import com.example.library.payload.request.LoginRequest;
import com.example.library.payload.request.RefreshTokenRequest;
import com.example.library.payload.request.SignupRequest;
import com.example.library.payload.response.JwtResponse;
import com.example.library.payload.response.MessageResponse;
import com.example.library.payload.response.RefreshTokenResponse;
import com.example.library.repository.security.RoleRepository;
import com.example.library.repository.security.UserRepository;
import com.example.library.security.JwtUtils;
import com.example.library.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    public ResponseEntity<MessageResponse> registerUser(SignupRequest signUpRequest) {
        ResponseEntity<MessageResponse> body = checkUserExist(signUpRequest);
        if (body != null) return body;
        User user = createUser(signUpRequest);
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = checkRole(strRoles);
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private ResponseEntity<MessageResponse> checkUserExist(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        return null;
    }

    private Set<Role> checkRole(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        return roles;
    }

    private User createUser(SignupRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        return user;
    }

    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {

        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new RefreshTokenResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
