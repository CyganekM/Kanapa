package com.senla.kanapa.service.security;

import com.senla.kanapa.entity.TokenBlack;
import com.senla.kanapa.repository.TokenBlackListJpaRepository;
import com.senla.kanapa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.time.LocalDateTime;
import java.util.List;


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] WHITE_LIST = {"/logins", "/swagger-ui/**", "/v3/**", "/users"};
    private static final String[] ADMIN_LIST = {"/**/admin_role", "/**/kanapic", "/categories", "/categories/**"};
    private final UserService service;
    private final AccessDeniedImpl accessDenied;
    private final TokenExtractData tokenExtractData;
    private final TokenBlackListJpaRepository tokenBlackListJpaRepository;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @EventListener(ApplicationReadyEvent.class)
    protected void addTokenBlackFromBaseInHashSet() {
        List<TokenBlack> tokenBlackList = tokenBlackListJpaRepository.findAll();
        if (!tokenBlackList.isEmpty()) {
            for (TokenBlack tokenBlack : tokenBlackList) {
                LocalDateTime date = LocalDateTime.now();
                String token = tokenBlack.getToken();
                if (tokenExtractData.extractDateExpiration(token).compareTo(date) > 0) {
                    JwtAuthorizationFilter.TOKEN_BLACK.add(token);
                } else {
                    tokenBlackListJpaRepository.delete(tokenBlack);
                }
            }
        }
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDenied)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests().antMatchers(WHITE_LIST).permitAll()
                .and()
                .authorizeRequests().antMatchers(ADMIN_LIST).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(super.authenticationManager(), resolver))
                .addFilter(new JwtAuthorizationFilter(super.authenticationManager()));
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(service);
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
}
