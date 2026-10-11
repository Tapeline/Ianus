package dev.tapeline.ianus.bootstrap

import liquibase.integration.spring.SpringLiquibase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
open class LiquibaseConfiguration {
    @Bean
    open fun liquibase(dataSource: DataSource?): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.setChangeLog("classpath:migrations/master.yml")
        liquibase.setDataSource(dataSource)
        return liquibase
    }
}