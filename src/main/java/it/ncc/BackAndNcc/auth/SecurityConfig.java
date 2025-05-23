package it.ncc.BackAndNcc.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Accesso libero a Swagger
                        //.requestMatchers("/api/**").permitAll()
                        .requestMatchers("/prenotazioni/by-date").hasRole("ADMIN")
                        .requestMatchers("/tour/delete/**").hasRole("ADMIN")
                        .requestMatchers("/prenotazioni/delete/**").hasRole("ADMIN")
                        .requestMatchers("/prenotazioni/update/**").hasRole("ADMIN")
                        .requestMatchers("/tour/update/**").hasRole("ADMIN")
                        .requestMatchers("/prenotazioni/price-calculation").permitAll()
                        .requestMatchers("/tour/price-calculation").permitAll()
                        .requestMatchers("/prenotazioni/bookNow").permitAll()
                        .requestMatchers("/tour/bookNow").permitAll()
                        .requestMatchers("/api/auth/verifyToken").permitAll()
                        .requestMatchers("/payments/create-payment-intent").permitAll()
                        .requestMatchers("/prenotazioni/by-id").hasRole("ADMIN")
                        .requestMatchers("/tour/by-date").hasRole("ADMIN")
                        .requestMatchers("/api/contact").permitAll()
                        .requestMatchers("/prenotazioni/maps-key").permitAll()
                        .requestMatchers("/prenotazioni/{id}/update-driver").hasRole("ADMIN")
                        .requestMatchers("/tour/{id}/update-driver").hasRole("ADMIN")
                        .requestMatchers("/prenotazioni/search").hasRole("ADMIN")
                        .requestMatchers("/tour/search").hasRole("ADMIN")



                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Aggiungi il filtro JWT
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
