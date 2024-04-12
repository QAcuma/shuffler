plugins {
    java
    application
    idea
    alias(libs.plugins.springframework)
    alias(libs.plugins.flyway)
}

val dbHost = System.getenv("SHUFFLER_DB_HOST") ?: "localhost" as String?
val dbPort = System.getenv("SHUFFLER_DB_PORT") ?: "5432" as String?
val dbName = System.getenv("SHUFFLER_DB_NAME") ?: "shuffler_local" as String?
val dbUser = System.getenv("SHUFFLER_DB_USER") ?: "local" as String?
val dbPassword = System.getenv("SHUFFLER_DB_PASSWORD") ?: "root" as String?

group = "ru.acuma"
version = "2.0.1"

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
    implementation(libs.spring.starter)
    implementation(libs.spring.web)
    implementation(libs.spring.aop)
    implementation(libs.spring.redis)
    implementation(libs.spring.data.jpa)
    implementation(libs.bundles.telegram)
    implementation(libs.bundles.data)
    implementation(libs.mapstruct)
    implementation(libs.caffeine)
    implementation(libs.caffeine.jcache)
    implementation(libs.hibernate.jcache)
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

flyway {
    driver = "org.postgresql.Driver"
    url = "jdbc:postgresql://$dbHost:$dbPort/$dbName"
    user = dbUser
    password = dbPassword
    cleanDisabled = false
    encoding = "UTF-8"
}
