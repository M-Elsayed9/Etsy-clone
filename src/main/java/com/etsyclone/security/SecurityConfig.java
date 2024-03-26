package com.etsyclone.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailsService userDetailsService;
    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/users/auth/**").permitAll()
                        .requestMatchers("/auth/login", "/auth/signout", "/auth/signup").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
//                        .requestMatchers(HttpMethod.DELETE, "/api/users").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/orders").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/products").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("SELLER")
//                        .requestMatchers(HttpMethod.POST, "/api/orders").hasRole("CUSTOMER")
//                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasRole("CUSTOMER")
//                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/api/orders").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public  JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}