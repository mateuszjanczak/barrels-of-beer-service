package com.mateuszjanczak.barrelsbeer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class HttpBasicAuthenticationAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password(encoder().encode("12345")).authorities("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // PROD
        //http.cors().and().csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();

        // DEV
        http.authorizeRequests().anyRequest().permitAll();
        http.cors().and().csrf().disable();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
