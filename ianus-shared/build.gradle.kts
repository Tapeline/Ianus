plugins {
    `kotlin-dsl`
}

dependencies {

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
    }
}

tasks.test {
    useJUnitPlatform()
}

configurations {
    runtimeClasspath {
        exclude(group = "org.gradle.internal.logging.slf4j", module = "gradle-api")
    }
}
