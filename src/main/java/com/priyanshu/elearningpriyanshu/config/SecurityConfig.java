package com.priyanshu.elearningpriyanshu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
//    @Autowired
//    private CustomAuthenticationProvider customAuthenticationProvider;
    private final CORSCustomizer corsCustomizer;

    public SecurityConfig(CORSCustomizer corsCustomizer) {
        this.corsCustomizer = corsCustomizer;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.corsCustomizer(http);
       return http
               .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/api/user/sign-up").permitAll()
                                .requestMatchers("/api/user/**").hasAnyRole("TRAINER","TRAINEE")
                                .requestMatchers("/api/trainer/**").hasRole("TRAINER")
                                .requestMatchers("api/trainee/**").hasRole("TRAINEE")
                                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**","/actuator/**").permitAll()
                                .anyRequest().authenticated()
                )
//               .authenticationProvider(customAuthenticationProvider)
                .httpBasic(Customizer.withDefaults()).build();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }
}
