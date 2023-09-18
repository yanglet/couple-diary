package com.couple.diary.presentation.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector


@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun passwordEncoder(): PasswordEncoder? = BCryptPasswordEncoder()

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? =
        WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().requestMatchers(AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(AntPathRequestMatcher("/favicon.ico"))
                .requestMatchers(AntPathRequestMatcher("/css/**"))
                .requestMatchers(AntPathRequestMatcher("/js/**"))
                .requestMatchers(AntPathRequestMatcher("/img/**"))
                .requestMatchers(AntPathRequestMatcher("/lib/**"))
                .requestMatchers(AntPathRequestMatcher("/swagger-ui/**"))
        }

    @Bean
    protected fun filterChain(http: HttpSecurity, introspector: HandlerMappingIntrospector?): SecurityFilterChain? =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(MvcRequestMatcher(introspector, "/**")).permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()
}