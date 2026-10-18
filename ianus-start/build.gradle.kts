plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":ianus-shared"))
    implementation(project(":ianus-account"))
    implementation(project(":ianus-connect"))
    implementation(project(":ianus-frontend"))
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterDataJdbc)
    implementation(libs.springBootStarterSecurity)
    implementation(libs.jteSpringBootStarter4)
    implementation(libs.jte)
    runtimeOnly(libs.h2)
    runtimeOnly(libs.postgresql)
    implementation(libs.liquibaseCore)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
    }
}