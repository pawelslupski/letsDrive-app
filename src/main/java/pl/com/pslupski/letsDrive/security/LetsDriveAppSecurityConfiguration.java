package pl.com.pslupski.letsDrive.security;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.com.pslupski.letsDrive.user.domain.UserEntityRepository;

@AllArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties(AdminConfig.class)
public class LetsDriveAppSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserEntityRepository repository;
    private final AdminConfig config;

    @Bean
    User systemUser() {
            return config.adminUser();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/cars/**", "/items/**", "/uploads/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/orders/**", "/login", "/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @SneakyThrows
    private JsonUsernameAuthenticationFilter authenticationFilter() {
        JsonUsernameAuthenticationFilter filter = new JsonUsernameAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManager());
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                    .withUser("jan@example.org")
//                    .password("{noop}xxx")
//                    .roles("USER")
//                .and()
//                    .withUser("admin")
//                    .password("{noop}xxx")
//                    .roles("ADMIN");
        auth.authenticationProvider(authenticationProvider());

    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        LetsDriveUserDetailsService userDetailsService = new LetsDriveUserDetailsService(repository, config);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}