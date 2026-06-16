
package com.clinica.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importação CORRETA
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {

                    req.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();


                    req.requestMatchers(HttpMethod.POST, "/autenticacao/login").permitAll();
                    req.requestMatchers("/error").permitAll();


                    req.requestMatchers(HttpMethod.GET, "/dentistas").permitAll();


                    // Permite que o DENTISTA altere o próprio status de férias
                    req.requestMatchers(HttpMethod.PUT, "/dentistas/{id}/entrar-ferias").hasAuthority("DENTISTA");
                    req.requestMatchers(HttpMethod.PUT, "/dentistas/{id}/retornar-ferias").hasAuthority("DENTISTA");

                    // Somente ADMIN pode mexer em usuários
                    req.requestMatchers("/usuarios/**").hasAuthority("ADMIN");

                    // Permitir que ADMIN e RECEPCAO acessem /pacientes/**
                    req.requestMatchers("/pacientes/**").hasAnyAuthority("ADMIN", "RECEPCAO"); // <-- CORREÇÃO DE PERMISSÃO AQUI!

                    // Somente ADMIN pode mexer no restante das operações de dentistas (POST, PUT, DELETE)

                    req.requestMatchers("/dentistas/**").hasAuthority("ADMIN");

                    // Demais rotas: qualquer usuário autenticado
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}