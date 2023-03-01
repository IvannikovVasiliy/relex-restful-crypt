package ru.relex.config;

import ru.relex.filter.JwtFilter;
import ru.relex.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/users/registration").permitAll()
                .requestMatchers("/users/login").permitAll()
                .requestMatchers("/admin/count-operations").hasAuthority("ADMIN")
                .requestMatchers("/currencies").hasAuthority("ADMIN")
                .requestMatchers("/balance/general-sum").hasAuthority("ADMIN")
                .requestMatchers("/users/test").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(genericFilterBean(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public GenericFilterBean genericFilterBean() {
        return new JwtFilter(jwtTokenProvider);
    }
}
