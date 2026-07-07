package mpma.mapa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableCaching
public class Configuracoes implements WebMvcConfigurer {
    @Value(
            "${app.cors.allowed-origins:http://localhost:3000}"
    )
    private String allowedOrigins;

    @Value("${app.docs.enabled:false}")
    private boolean docsEnabled;

    @Override
    public void addCorsMappings (CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins(parseAllowedOrigins())
                .allowedMethods("GET", "OPTIONS")
                .allowedHeaders("*");
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.requestMatchers("/actuator/health").permitAll();

                    if (docsEnabled) {
                        auth.requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll();
                    }

                    auth.requestMatchers(HttpMethod.GET,
                            "/assistencia_social/**",
                            "/clusterizacoes/**",
                            "/demograficos/**",
                            "/economicos/**",
                            "/educacao/**",
                            "/estadual/**",
                            "/geograficos/**",
                            "/informacoes/**",
                            "/predicoes/**",
                            "/saude/**"
                    ).permitAll();
                    auth.anyRequest().denyAll();
                });
        return http.build();
    }

    private String[] parseAllowedOrigins() {
        return Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isBlank())
                .toArray(String[]::new);
    }
}
