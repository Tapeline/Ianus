plugins {
    `kotlin-dsl`
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.openapiprocessor.openapi-processor") version "2026.2"
    id("gg.jte.gradle") version("3.2.4")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.nimbusds:nimbus-jose-jwt:10.0.2")
    implementation("org.springframework.security:spring-security-crypto:6.4.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    compileOnly("io.openapiprocessor:openapi-processor-spring:2026.3.2")
    implementation("org.liquibase:liquibase-core:4.27.0")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")
    implementation("gg.jte:jte-spring-boot-starter-4:3.2.4")
    implementation("gg.jte:jte:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-mail:3.5.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.2.0")
    testImplementation("io.kotest:kotest-framework-engine:6.0.7")
    testImplementation("io.kotest:kotest-assertions-core:6.0.7")
    testImplementation("io.kotest:kotest-property:6.0.7")
    testImplementation("io.kotest:kotest-runner-junit5:6.0.7")
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

jte {
    precompile()
}

openapiProcessor {
    apiPath("${projectDir}/src/api/openapi/account.yml")

    process("spring") {
        processor("io.openapiprocessor:openapi-processor-spring:2026.3.2")
        prop("mapping", "${projectDir}/src/api/mapping.yml")
        targetDir(layout.buildDirectory.dir("$projectDir/build/openapi"))
    }
}

sourceSets {
    create("api") {
        resources {
            srcDir("${projectDir}/src/api")
        }
    }

    main {
        java {
            srcDir(tasks.named("processSpring"))
        }
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
