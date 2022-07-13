package com.mwork.main.config;

import com.mwork.main.auth.JwtAuthenticationFilter;
import com.mwork.main.auth.Oauth2Service;
import com.mwork.main.auth.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final Oauth2Service oauth2Service;
    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/**").permitAll()
                .anyRequest().authenticated().and().formLogin().disable();

        http.addFilterBefore(new JwtAuthenticationFilter(tokenService,oauth2Service), UsernamePasswordAuthenticationFilter.class);

        http.logout().deleteCookies("JSESSIONID");
        http.logout().deleteCookies("accessToken");

        return http.build();
    }

}
