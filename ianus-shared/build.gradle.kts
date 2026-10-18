plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterDataJdbc)
    implementation(libs.kotlinLoggingJvm)
    runtimeOnly(libs.h2)
    runtimeOnly(libs.postgresql)
    implementation(libs.liquibaseCore)
    compileOnly(libs.openapiProcessorSpring)
    implementation(libs.snakeyaml)
    implementation(libs.springBootStarterValidation)
    implementation(libs.validationApi)
    implementation(libs.kotlinReflect)
    implementation(libs.nimbusJoseJwt)
    implementation(libs.springSecurityCrypto)
    implementation(libs.bouncycastleBcprovJdk18on)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
    }
}

tasks.test {
    useJUnitPlatform()
}
