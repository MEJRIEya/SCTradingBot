package com.solidcode.SCTradingBot.security;

import com.solidcode.SCTradingBot.security.filter.CustomAuthenticationFilter;
import com.solidcode.SCTradingBot.security.filter.CustomAuthorizationFilter;
import com.solidcode.SCTradingBot.security.permission.PermissionService;
import com.solidcode.SCTradingBot.security.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PermissionService permissionService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           UserRepo userRepo) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), userRepo);
        customAuthenticationFilter.setFilterProcessesUrl("/token");

        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));
        http.authorizeHttpRequests(request -> request.requestMatchers("/token/**").permitAll());
        //http.authorizeHttpRequests(request -> request.requestMatchers("/**").hasAuthority("ALL"));
        loadApiAccessPermissions(http);
        http.authorizeHttpRequests(request -> request.requestMatchers("/**")
                .permitAll().anyRequest().authenticated());
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    private void loadApiAccessPermissions(HttpSecurity http) {
        permissionService.getAllPermission().forEach(permission -> {
            try {
                http.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.valueOf(permission.getHttpMethod()), permission.getPath() + "/**").hasAuthority(permission.getDescription()));
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
