package it.bitrock.bitrockairways.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public HttpSessionSecurityContextRepository sessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers("/login",  "/failure").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .redirectionEndpoint().baseUri("/success.html")
                .and()
                .defaultSuccessUrl("/success",true)
                .failureUrl("/failure");

        return http.build();

        /*http.authorizeHttpRequests()
                .requestMatchers("/", "/success", "success.html").authenticated()
                .anyRequest()
                .permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .redirectionEndpoint().baseUri("/success.html")
                .and()
                .defaultSuccessUrl("/success",true)
                ;
        return http.build();*/

    }


}
