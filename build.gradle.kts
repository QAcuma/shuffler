plugins {
    java
    application
    idea
    id("org.springframework.boot") version "2.7.3"
}

group = "ru.acuma"
version = "2.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("ru.acuma:shuffler-lib:2.0.0")
    implementation(libs.spring.starter)
    implementation(libs.spring.web)
    implementation(libs.spring.aop)
    implementation(libs.bundles.telegram)
    implementation(libs.bundles.data)
    implementation(libs.jooq)
    implementation(libs.mapstruct)
    implementation(libs.bundles.util)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.mapstruct.processor)
    annotationProcessor(libs.mapstruct.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.bundles.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    applicationDefaultJvmArgs = listOf(
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED"
    )
}
