package com.medilinkpro.backend.config;

import com.medilinkpro.backend.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration centrale de Spring Security :
 * - API stateless (pas de session, authentification par JWT a chaque requete)
 * - Autorisations par role sur les endpoints metier
 * - Swagger UI et endpoints d'authentification ouverts publiquement
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**",
            "/api/urgence/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        // Module Geolocalisation & Etablissements : lecture ouverte aux roles authentifies
                        .requestMatchers("/api/medecins/recherche", "/api/etablissements/**").authenticated()

                        // Administration
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Tableau de bord / statistiques : Directeur et Admin
                        .requestMatchers("/api/dashboard/**").hasAnyRole("DIRECTEUR", "ADMIN")

                        // Dossiers medicaux : Patient, Medecin, Secretaire, Admin
                        .requestMatchers("/api/dossiers-medicaux/**")
                        .hasAnyRole("PATIENT", "MEDECIN", "SECRETAIRE", "ADMIN")

                        // Consultations et ordonnances : Medecin, Patient (lecture), Secretaire, Admin
                        .requestMatchers("/api/consultations/**", "/api/ordonnances/**")
                        .hasAnyRole("MEDECIN", "PATIENT", "SECRETAIRE", "ADMIN")

                        // Rendez-vous : Patient, Medecin, Secretaire, Admin
                        .requestMatchers("/api/rendez-vous/**")
                        .hasAnyRole("PATIENT", "MEDECIN", "SECRETAIRE", "ADMIN")

                        // Gestion des patients et medecins (CRUD complet) : Secretaire, Admin, Directeur
                        .requestMatchers("/api/patients/**").hasAnyRole("PATIENT", "MEDECIN", "SECRETAIRE", "ADMIN")
                        .requestMatchers("/api/medecins/**").hasAnyRole("MEDECIN", "SECRETAIRE", "ADMIN", "DIRECTEUR")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
