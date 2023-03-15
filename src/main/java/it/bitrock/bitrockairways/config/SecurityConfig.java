package it.bitrock.bitrockairways.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

/*    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().anyRequest().authenticated().and().oauth2Login()
                .redirectionEndpoint()
                .baseUri("/success.html")
                .and()
                .defaultSuccessUrl("/login/success");
        return http.build();

        http.authorizeHttpRequests()
                .requestMatchers("/login", "/login.html").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                //.and()
                //.formLogin()
                //.loginPage("/login")
                .defaultSuccessUrl("/login/success");

        return http.build();
    }*/


    /*@Bean
    public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .anyRequest().permitAll()
                .and().oauth2Login()
                .redirectionEndpoint().baseUri("/success.html")
                .and().defaultSuccessUrl("/loginSuccess")
                .failureHandler(new SimpleUrlAuthenticationFailureHandler("/loginFailure"))
                .and().build();
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
        return http.build();
    }

}
