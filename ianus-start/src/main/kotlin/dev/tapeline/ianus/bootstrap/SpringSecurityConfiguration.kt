package dev.tapeline.ianus.bootstrap

import dev.tapeline.ianus.presentation.security.IanusAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
open class SpringSecurityConfiguration {

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            authorizeHttpRequests {
                authorize("/**", permitAll)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(IanusAuthenticationFilter())
        }
        return http.build()
    }

}
