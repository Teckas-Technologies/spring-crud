package com.example.Demo.service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * Auth class
     * @param username username to verify
     * @return if username is valid
     * @throws UsernameNotFoundException throws exception if it is invalid
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Mock user data; replace with database fetching logic
        if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password("{noop}password") // Use bcrypt for production
                    .roles("ADMIN")
                    .build();
        } else if ("user".equals(username)) {
            return User.withUsername("user")
                    .password("{noop}password")
                    .roles("USER")
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}

