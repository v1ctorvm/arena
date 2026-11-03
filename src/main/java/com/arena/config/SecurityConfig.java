package com.arena.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // desativa temporariamente a proteção CSRF, que poderia bloquear nossos testes de POST.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // permite acessar qualquer endpoint sem autenticação.
                );

        return http.build(); // constrói e devolve a configuração de segurança ao Spring.
    }
}
