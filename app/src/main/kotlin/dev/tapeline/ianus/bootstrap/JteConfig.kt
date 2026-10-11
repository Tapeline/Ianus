package dev.tapeline.ianus.bootstrap

import gg.jte.TemplateEngine
import gg.jte.ContentType
import gg.jte.resolve.DirectoryCodeResolver
import gg.jte.springframework.boot.autoconfigure.JteProperties
import gg.jte.springframework.boot.autoconfigure.JteViewResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import java.nio.file.Paths

@Configuration
open class TemplateConfig {
    @Bean
    open fun jteViewResolver(templateEngine: TemplateEngine) =
        JteViewResolver(templateEngine, JteProperties()).apply {
            setSuffix(".jte")
            order = Ordered.HIGHEST_PRECEDENCE
        }
}
