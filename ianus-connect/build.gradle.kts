plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.openapiProcessor)
}

dependencies {
    implementation(project(":ianus-shared"))
    implementation(libs.springBootStarterWeb)
    implementation(libs.nimbusJoseJwt)
    implementation(libs.springSecurityCrypto)
    implementation(libs.springBootStarterDataJdbc)
    implementation(libs.kotlinLoggingJvm)
    runtimeOnly(libs.h2)
    runtimeOnly(libs.postgresql)
    implementation(libs.liquibaseCore)
    compileOnly(libs.openapiProcessorSpring)
    implementation(libs.snakeyaml)
    implementation(libs.springBootStarterValidation)
    implementation(libs.validationApi)
    implementation(libs.springBootStarterSecurity)
    implementation(libs.bouncycastleBcprovJdk18on)
    implementation(libs.jteSpringBootStarter4)
    implementation(libs.jte)
    implementation(libs.springBootStarterMail)
    implementation(libs.kotlinReflect)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.kotlinTestJunit)
    testImplementation(libs.kotestFrameworkEngine)
    testImplementation(libs.kotestAssertionsCore)
    testImplementation(libs.kotestProperty)
    testImplementation(libs.kotestRunnerJunit5)
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

openapiProcessor {
    apiPath("${projectDir}/src/api/openapi/connect.yml")

    process("spring") {
        processor("${libs.openapiProcessorSpring.get().module}:${libs.versions.openapiProcessorSpring.get()}")
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
