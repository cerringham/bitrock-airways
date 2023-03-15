package it.bitrock.bitrockairways.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .anyRequest().permitAll()
                .and().oauth2Login()
                .redirectionEndpoint().baseUri("/success.html")
                .and().defaultSuccessUrl("/loginSuccess")
                .failureHandler(new SimpleUrlAuthenticationFailureHandler("/loginFailure"))
                .and().build();
    }
}
