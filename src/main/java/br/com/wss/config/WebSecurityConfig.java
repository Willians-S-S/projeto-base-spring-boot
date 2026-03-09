package br.com.wss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import br.com.wss.projeto.resources.AccountResource;
import br.com.wss.projeto.resources.AuthenticationResource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.POST, AuthenticationResource.AUTHENTICATE).permitAll()
                                .requestMatchers(HttpMethod.GET, AccountResource.ACCOUNTS).authenticated()
                                .requestMatchers(HttpMethod.PUT, AccountResource.ACCOUNTS).authenticated()
                                .requestMatchers(HttpMethod.POST, AccountResource.CREATE_ACCOUNTS).permitAll()
                                .requestMatchers(HttpMethod.POST, AccountResource.CREATE_ACCOUNTS_ROLES).hasAuthority("SCOPE_ROLE_ADM")
                                .requestMatchers(HttpMethod.GET, AccountResource.FIND_ALL).permitAll() //.hasAuthority("SCOPE_ROLE_ADM")
                                .requestMatchers(HttpMethod.GET, AccountResource.FIND_PARAM).permitAll() //.hasAuthority("SCOPE_ROLE_ADM")
                                .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                                .anyRequest().authenticated()
                ).oauth2ResourceServer(
                        conf -> conf.jwt(Customizer.withDefaults())
                );
        return http.build();
    }
}
