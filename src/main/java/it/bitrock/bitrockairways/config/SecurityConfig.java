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
                .requestMatchers("/login", "/login.html"
                 ,"/success.html").permitAll()
                //.anyRequest()
                //.requestMatchers("/success", "/success.html")
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                //.and()
                //.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/success",true)
        ;
        return http.build();

        /*http.authorizeHttpRequests().anyRequest().authenticated().and().oauth2Login();
        return http.build();*/
    }


}
