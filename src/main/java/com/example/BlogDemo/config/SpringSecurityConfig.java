package com.example.BlogDemo.config;

import com.example.BlogDemo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SpringSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public SpringSecurityConfig(boolean disableDefaults, UserDetailsServiceImpl userDetailsService) {
        super(disableDefaults);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic()
                    .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/home").permitAll()
                    .antMatchers("/login*").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/posts").hasAuthority("USER")
                    .antMatchers(HttpMethod.PUT, "/api/v1/posts/**").hasAuthority("USER")
                    .antMatchers(HttpMethod.DELETE, "/api/v1/posts/**").hasAuthority("USER")
                    .antMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                    .antMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAuthority("USER")
                    .antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("ADMIN")
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home")
                    .failureUrl("/login?error=true")
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .permitAll()
                    .and()
                .csrf().disable();



    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
