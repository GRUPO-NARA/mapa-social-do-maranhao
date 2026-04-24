package mpma.mapasocial.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class configuracoesDeSeguranca {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth -> auth
                // Qualquer requisição está permitida (desenvolvimento)
                .anyRequest().permitAll());
        return http.build();
    }

}
