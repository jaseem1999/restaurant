package com.restaurant.user_service.security;

import com.restaurant.user_service.dto.user_credential.UserCredentialDto;
import com.restaurant.user_service.projection.use_credential.UserCredentialProjection;
import com.restaurant.user_service.repository.user.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<String> roles = Arrays.asList(userCredential.getRoles().split(","));
        if (userCredential != null) {
            return new UserPrincipal(userCredential.getId(),userCredential.getEmail(), userCredential.getPassword(), roles);
        }
        return null;
    }
}
