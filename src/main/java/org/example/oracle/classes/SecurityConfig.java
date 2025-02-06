package org.example.oracle.classes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()  // Exige l'authentification pour toute requête
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Page de connexion personnalisée
                        .permitAll()  // Permet l'accès à la page de connexion sans authentification
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL de déconnexion
                        .logoutSuccessUrl("/login?logout")  // Redirige après déconnexion
                        .permitAll()  // Permet l'accès à la déconnexion sans authentification
                )
                .csrf(csrf -> csrf.disable());  // Désactive la protection CSRF

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("sys")
                .password(passwordEncoder.encode("oracle"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
