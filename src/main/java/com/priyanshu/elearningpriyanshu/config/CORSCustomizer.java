package com.priyanshu.elearningpriyanshu.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Component
public class CORSCustomizer {


        // @Value("${api.gateway.path}")
        public String origin;

        public void corsCustomizer(HttpSecurity http) throws Exception {
            http.cors(c -> {
                CorsConfigurationSource source = s -> {
                    CorsConfiguration cc = new CorsConfiguration();
                    cc.setAllowCredentials(true);
//                    cc.setAllowedOrigins(List.of(""));
                    cc.setAllowedOriginPatterns(List.of(CorsConfiguration.ALL));
                    //  cc.setAllowedOrigins(List.of("*"));
                    cc.setAllowedHeaders(List.of("*"));
                  //  cc.setAllowedMethods(List.of("*"));
                    cc.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    return cc;
                };
                c.configurationSource(source);
            });
        }
    }
