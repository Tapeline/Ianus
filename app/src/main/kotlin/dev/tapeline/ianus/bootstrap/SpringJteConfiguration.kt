package dev.tapeline.ianus.bootstrap

import gg.jte.TemplateEngine
import gg.jte.springframework.boot.autoconfigure.JteProperties
import gg.jte.springframework.boot.autoconfigure.JteViewResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered

@Configuration
open class SpringJteConfiguration {

    @Bean
    open fun jteViewResolver(templateEngine: TemplateEngine) =
        JteViewResolver(templateEngine, JteProperties()).apply {
            setSuffix(".jte")
            order = Ordered.HIGHEST_PRECEDENCE
        }

}
