package javaweb.shopping_cart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/", "/home", "/login", "/403","/css/**","/images/**").permitAll()
                .requestMatchers("/registration","/webjars/**").permitAll()
                .requestMatchers("/products").permitAll()
                .anyRequest().authenticated()
        )
        .formLogin((formBased) -> {
            formBased
                    .loginPage("/login")
                    .defaultSuccessUrl("/products").permitAll()
                    .failureUrl("/login?error=true").permitAll();
                }
        ).logout((logout) -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
        ).exceptionHandling(
                except -> except
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .accessDeniedPage("/403")
        );

        return http.build();
    }

}
