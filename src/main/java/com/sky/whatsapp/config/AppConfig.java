package com.sky.whatsapp.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class AppConfig {

    @Bean
    public SecurityFilterChain securityConfiguration(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf().disable()
                .cors().configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();

                    corsConfig.setAllowedOrigins(Arrays.asList(
                            "http://localhost:3000",
                            "http://localhost:4200"
                    ));
                    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowCredentials(true);
                    corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                    corsConfig.setExposedHeaders(Arrays.asList("Authorization"));
                    corsConfig.setMaxAge(3600L);

                    return corsConfig;
                })
                .and().formLogin().and().httpBasic();

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
