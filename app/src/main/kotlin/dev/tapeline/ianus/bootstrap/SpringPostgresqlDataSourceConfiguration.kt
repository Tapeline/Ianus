package dev.tapeline.ianus.bootstrap

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class SpringPostgresqlDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("app.db")
    fun dataSource() =
        DataSourceBuilder.create().build()

}
