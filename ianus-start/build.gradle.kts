plugins {
    `kotlin-dsl`
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.6"
}

dependencies {
    implementation(project(":ianus-shared"))
    implementation(project(":ianus-account"))
    implementation(project(":ianus-connect"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
    }
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    mainClass = "dev.tapeline.ianus.IanusApp"
    classpath = classpath.filter { file ->
        !file.name.contains("gradle-api") && !file.name.contains("gradle-installation-beacon")
    }
}

configurations {
    runtimeClasspath {
        exclude(group = "org.gradle.internal.logging.slf4j", module = "gradle-api")
    }
}