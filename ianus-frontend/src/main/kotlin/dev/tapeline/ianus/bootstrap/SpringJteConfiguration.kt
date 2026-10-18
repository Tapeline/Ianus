package dev.tapeline.ianus.bootstrap

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.resolve.ResourceCodeResolver
import gg.jte.springframework.boot.autoconfigure.JteProperties
import gg.jte.springframework.boot.autoconfigure.JteViewResolver
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths

@Configuration
open class SpringJteConfiguration(
    private val templateEngineProvider: ObjectProvider<TemplateEngine>,
    private val jteProperties: JteProperties
) : WebMvcConfigurer {

    @Bean
    open fun jteViewResolver(templateEngine: TemplateEngine) =
        JteViewResolver(templateEngine, jteProperties).apply {
            order = Ordered.HIGHEST_PRECEDENCE
        }

    @Bean
    open fun jteTemplateEngine(): TemplateEngine =
        if (jteProperties.isDevelopmentMode) {
            TemplateEngine.create(
                ResourceCodeResolver("jte"),
                Paths.get("jte-classes"),
                ContentType.Html,
                javaClass.classLoader
            )
        } else {
            TemplateEngine.createPrecompiled(ContentType.Html)
        }

}
