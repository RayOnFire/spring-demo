package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by Ray on 2017/6/30.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    private CsrfFilter csrfFilter;
    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;
    private RememberMeAuthenticationFilter rememberMeAuthenticationFilter;

    private RememberMeServices rememberMeServices;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST)
                .authenticated()
                .antMatchers(HttpMethod.PUT)
                .authenticated()
                .antMatchers(HttpMethod.DELETE)
                .authenticated()
                .and()
                .authenticationProvider(rememberMeAuthenticationProvider())
                .authenticationProvider(authenticationProvider())
                .csrf().disable().headers().frameOptions().sameOrigin().and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/custom-login")
                .and()
                .addFilter(getCsrfFilter())
                .addFilter(getUsernamePasswordAuthenticationFilter())
                .addFilter(getRememberMeAuthenticationFilter());
    }

    private UsernamePasswordAuthenticationFilter getUsernamePasswordAuthenticationFilter() throws Exception {
        if (usernamePasswordAuthenticationFilter != null) {
            return usernamePasswordAuthenticationFilter;
        }
        UsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordFilter(authenticationManagerBean());
        filter.setRememberMeServices(getRememberMeServices());
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerImpl());
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandlerImpl());

        this.usernamePasswordAuthenticationFilter = filter;
        return filter;
    }

    private RememberMeServices getRememberMeServices() {
        if (this.rememberMeServices != null) {
            return this.rememberMeServices;
        }
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("rememberme", userDetailsService);
        rememberMeServices.setParameter("remember-me");
        rememberMeServices.setTokenValiditySeconds(86400);
        rememberMeServices.setCookieName("rememberme");
        rememberMeServices.setCookieDomain("rememberme");
        this.rememberMeServices = rememberMeServices;
        return rememberMeServices;
    }

    private RememberMeAuthenticationFilter getRememberMeAuthenticationFilter() throws Exception {
        if (this.rememberMeAuthenticationFilter != null) {
            return this.rememberMeAuthenticationFilter;
        }
        RememberMeAuthenticationFilter rememberMeAuthenticationFilter = new RememberMeAuthenticationFilter(authenticationManagerBean(), getRememberMeServices());
        this.rememberMeAuthenticationFilter = rememberMeAuthenticationFilter;
        return rememberMeAuthenticationFilter;
    }

    private CsrfTokenRepository getCsrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    private CsrfFilter getCsrfFilter() {
        CsrfFilter csrfFilter = new CsrfFilter(getCsrfTokenRepository());
        csrfFilter.setRequireCsrfProtectionMatcher(new AntPathRequestMatcher("/custom-login", "POST"));
        return csrfFilter;
    }
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider() { return new RememberMeAuthenticationProvider("rememberme"); }

    // @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        //authenticationProvider.setPasswordEncoder(passwordEncoder());
        // TODO: add hash password support
        return authenticationProvider;
    }


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    /*
    @Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        PersistentTokenBasedRememberMeServices tokenBasedservice = new PersistentTokenBasedRememberMeServices(
                "remember-me", userDetailsService, persistentTokenRepository);
        return tokenBasedservice;
    }
    */

    /*
    @Bean
    public AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }
    */
}
