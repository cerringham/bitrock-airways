package it.bitrock.bitrockairways.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/login/success").authenticated()
                .anyRequest().permitAll()
                .and().oauth2Login()
                .loginPage("/login")
                .redirectionEndpoint().baseUri("/success.html")
                .and().defaultSuccessUrl("/login/success");
        return http.build();
    }

}
