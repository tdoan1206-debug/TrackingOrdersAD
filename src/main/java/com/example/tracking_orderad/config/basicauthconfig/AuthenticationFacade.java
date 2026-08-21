package com.example.tracking_orderad.config.basicauthconfig;

import com.example.tracking_orderad.entity.User;
import com.example.tracking_orderad.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
    private final UserRepo userRepo;

    public User getCurrentUser() {
        // Lay thong tin user dang dang nhap
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}
