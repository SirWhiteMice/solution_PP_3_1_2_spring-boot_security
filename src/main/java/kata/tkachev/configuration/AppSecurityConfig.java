package kata.tkachev.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class AppSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationSuccessHandler loginSuccessHandler) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .usernameParameter("email")
                        .successHandler(loginSuccessHandler).permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
