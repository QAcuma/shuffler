plugins {
    id("java")
    id("application")
    id("idea")
    id("org.springframework.boot") version "2.6.1"
}

group = "ru.acuma"
version = "1.0.7"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenLocal()
    mavenCentral()
}

var springBootVersion = "2.6.7"
var lombokBootVersion = "1.18.24"
var lang3Version = "3.12.0"
var log4jVersion = "1.2.17"
var junitVersion = "5.8.1"
var telegramBotVersion = "6.0.1"
val codeGsonVersion = "2.9.0"
val orikaVersion = "2.2.7"

dependencies {
    implementation("ru.acuma:shuffler-lib:1.0.0")

    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("dev.akkinoc.spring.boot:orika-spring-boot-starter:$orikaVersion")

    implementation("org.telegram:telegrambots:$telegramBotVersion")
    implementation("org.telegram:telegrambotsextensions:$telegramBotVersion")

    implementation("com.google.code.gson:gson:$codeGsonVersion")
    implementation("org.apache.commons:commons-lang3:$lang3Version")

    compileOnly("org.projectlombok:lombok:$lombokBootVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokBootVersion")

    testCompileOnly("org.projectlombok:lombok:$lombokBootVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokBootVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.mockito:mockito-core:4.5.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    applicationDefaultJvmArgs = listOf("---add-opens java.base/java.lang=ALL-UNNAMED")
}
