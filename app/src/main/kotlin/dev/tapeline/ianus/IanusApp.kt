package dev.tapeline.ianus

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@SpringBootApplication
@ConfigurationPropertiesScan
class IanusApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(IanusApp::class.java)
        }
    }
}
