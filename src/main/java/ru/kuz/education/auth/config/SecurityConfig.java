package ru.kuz.education.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.kuz.education.auth.service.MyUserDetailsService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers("/students/**").hasRole("USER")
                        .requestMatchers("/images/**").hasRole("USER, TEACHER")
                        .requestMatchers("/teachers/**").hasRole("TEACHER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .loginPage("/login")
//                        .defaultSuccessUrl("/", true)
                                .successHandler(roleBasedSuccessHandler()) // Обработчик успешной аутентификации
                                .failureUrl("/login?error=true") // Передача параметра ошибки
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true") // Передача параметра успешного выхода
                        .permitAll()
                );

        return http.build();
    }


    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return new RoleBasedSuccessHandler();
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder.encode("admin")).roles("ADMIN").build();
//        UserDetails user =
//                User.builder().username("user").password(passwordEncoder.encode("123")).roles("USER").build();
//        UserDetails root =
//                User.builder().username("root").password(passwordEncoder.encode("root")).roles("ADMIN, USER").build();
//        return new InMemoryUserDetailsManager(admin, user, root);
//    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        return new MyUserDetailsService();
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
