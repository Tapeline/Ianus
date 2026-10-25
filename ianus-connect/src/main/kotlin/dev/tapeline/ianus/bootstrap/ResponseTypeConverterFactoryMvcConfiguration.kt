package dev.tapeline.ianus.bootstrap

import dev.tapeline.ianus.presentation.openapi.model.ResponseType
import dev.tapeline.ianus.presentation.openapi.spring.StringToEnumConverterFactory

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class ResponseTypeConverterFactoryMvcConfiguration : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) =
        registry.addConverterFactory(StringToEnumConverterFactory<ResponseType>())

}
