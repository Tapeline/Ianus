package dev.tapeline.ianus

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

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
