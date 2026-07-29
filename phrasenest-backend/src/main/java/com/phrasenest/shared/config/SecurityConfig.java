package com.phrasenest.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                // Temporary for the initial REST API. We will configure CORS and CSRF
//                // properly when JWT authentication and the React frontend are added.
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/v1/public/**",
//                                "/actuator/health",
//                                "/actuator/info"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults())
//                .build();
//    }
//}


// updated Security config



@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http
                /*
                 * Temporarily disabled for our stateless REST testing.
                 * We will revisit CSRF when authentication is finalized.
                 */
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Public browsing endpoints.
                        .requestMatchers(
                                "/api/v1/public/**",
                                "/actuator/health",
                                "/actuator/info"
                        )
                        .permitAll()

                        // Only administrators can manage expression content.
                        .requestMatchers("/api/v1/admin/**")
                        .hasRole("ADMIN")

                        // Any other endpoint requires authentication.
                        .anyRequest()
                        .authenticated()
                )

                // Temporary local authentication.
                .httpBasic(Customizer.withDefaults())

                .build();
    }
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
