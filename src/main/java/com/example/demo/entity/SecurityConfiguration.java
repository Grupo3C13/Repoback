package com.example.demo.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .csrf(csrf ->
                        csrf
                                .disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
//                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/doc/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/auth/**").permitAll()

                                .requestMatchers("/products/listar").permitAll()
                                .requestMatchers("/products/listarexpress").permitAll()
                                .requestMatchers("/products/{id}").permitAll()

                                .requestMatchers("/characteristics/listar").permitAll()
                                .requestMatchers("/characteristics/{id}").permitAll()

                                .requestMatchers("/categories/listar").permitAll()
                                .requestMatchers("/categories/{id}").permitAll()

                                .requestMatchers("/products/add").hasAuthority("ADMIN")
                                .requestMatchers("/products/eliminar/**").hasAuthority("ADMIN")
                                .requestMatchers("/products/modificar").hasAuthority("ADMIN")
                                .requestMatchers("/{productsId}/categories/{categoriesId}").hasAuthority("ADMIN")
                                .requestMatchers("/{productsId}/characteristics/{characteristicsId}").hasAuthority("ADMIN")

                                .requestMatchers("/reservation/product/**").permitAll()
                                .requestMatchers("/reservation/listar").permitAll()
                                .requestMatchers("/review/product/**").permitAll()

                                .requestMatchers("/reservation/eliminar/**").hasAuthority("ADMIN")
                                .requestMatchers("/reservation/modificar").hasAuthority("ADMIN")

                                .requestMatchers("/characteristics/agregar").hasAuthority("ADMIN")
                                .requestMatchers("/characteristics/eliminar/**").hasAuthority("ADMIN")
                                .requestMatchers("/characteristics/modificar").hasAuthority("ADMIN")

                                .requestMatchers("/categories/agregar").hasAuthority("ADMIN")
                                .requestMatchers("/categories/eliminar/**").hasAuthority("ADMIN")
                                .requestMatchers("/categories/modificar").hasAuthority("ADMIN")

                                .requestMatchers("/user/agregar").hasAuthority("ADMIN")
                                .requestMatchers("/user/eliminar/**").hasAuthority("ADMIN")
                                .requestMatchers("/user/modificar").hasAuthority("ADMIN")

                                .anyRequest().authenticated()
                )
                .cors(withDefaults())
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)


                .build();
    }
}