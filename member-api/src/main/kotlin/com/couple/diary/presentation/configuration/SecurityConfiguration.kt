package com.couple.diary.presentation.configuration

import com.couple.diary.application.security.JwtService
import com.couple.diary.application.security.PrincipalDetailsService
import com.couple.diary.application.security.filter.JwtAuthenticationFilter
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val jwtService: JwtService,
    private val principalDetailsService: PrincipalDetailsService
) {
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
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain? =
        http
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/v1/members/join", "/v1/members/login").anonymous()
                    .requestMatchers("/v1/members/**").hasRole("MEMBER")
                    .anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(
                JwtAuthenticationFilter(jwtService, principalDetailsService),
                JwtAuthenticationFilter::class.java
            )
            .build()
}