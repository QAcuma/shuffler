plugins {
    java
    application
    idea
    id("org.springframework.boot") version "2.7.0"
}

group = "ru.acuma"
version = "1.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_13
    targetCompatibility = JavaVersion.VERSION_13
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(libs.shuffler.lib)
    implementation(libs.spring.starter)
    implementation(libs.spring.web)
    implementation(libs.bundles.telegram)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testImplementation(libs.bundles.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    applicationDefaultJvmArgs = listOf("---add-opens java.base/java.lang=ALL-UNNAMED")
}
