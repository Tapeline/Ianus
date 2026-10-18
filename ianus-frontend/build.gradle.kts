plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.jte)
}

dependencies {
    implementation(libs.springBootStarterWeb)
    implementation(libs.snakeyaml)
    implementation(libs.validationApi)
    implementation(libs.jteSpringBootStarter4)
    implementation(libs.jte)
    implementation(libs.kotlinReflect)
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

jte {
    precompile()
}

