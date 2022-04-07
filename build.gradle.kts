plugins {
    id("java")
    id("java-library")
    id("application")
    id("idea")
    id("org.springframework.boot") version "2.6.1"
    id("org.flywaydb.flyway") version "8.2.3"
    id("nu.studer.jooq") version "5.2"
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

val dbHost = System.getenv("K_SHUFFLER_DB_SERVER_HOST") ?: "localhost" as String?
val dbPort = System.getenv("K_SHUFFLER_DB_SERVER_PORT") ?: "5432" as String?
val dbName = System.getenv("K_SHUFFLER_DB_NAME") ?: "k_shuffler" as String?
val dbUser = System.getenv("K_SHUFFLER_DB_USER") ?: "k_shuffler" as String?
val dbPassword = System.getenv("K_SHUFFLER_DB_PASSWORD") ?: "root" as String?

var springBootVersion = "2.6.1"
var lombokBootVersion = "1.18.22"
var lang3Version = "3.12.0"
var log4jVersion = "1.2.17"
var junitVersion = "5.8.1"
var telegramBotVersion = "5.5.0"
var psqlVersion = "42.3.1"
var flywayVersion = "8.2.3"
var jooqVersion = "3.15.5"
val codeGsonVersion = "2.8.9"
val orikaVersion = "2.2.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-jooq:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-jooq:$springBootVersion")
    implementation("dev.akkinoc.spring.boot:orika-spring-boot-starter:$orikaVersion")

    implementation("org.telegram:telegrambots:$telegramBotVersion")
    implementation("org.telegram:telegrambotsextensions:$telegramBotVersion")

    implementation("org.postgresql:postgresql:$psqlVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.jooq:jooq:$jooqVersion")
    implementation("com.google.code.gson:gson:$codeGsonVersion")
    compileOnly("org.jooq:jooq-meta:$jooqVersion")
    compileOnly("org.jooq:jooq-codegen:$jooqVersion")
    jooqGenerator("org.postgresql:postgresql:$psqlVersion")

    implementation("org.apache.commons:commons-lang3:$lang3Version")

    compileOnly("org.projectlombok:lombok:$lombokBootVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokBootVersion")
}

flyway {
    driver = "org.postgresql.Driver"
    url = "jdbc:postgresql://$dbHost:$dbPort/$dbName"
    user = dbUser
    password = dbPassword
    cleanDisabled = false
}

jooq {
    version.set(jooqVersion)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.ERROR
                onError = org.jooq.meta.jaxb.OnError.LOG
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://$dbHost:$dbPort/$dbName"
                    user = dbUser
                    password = dbPassword
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = "(.*_[0-9]+|.*_default)"
                        forcedTypes.addAll(
                            arrayOf(
                                org.jooq.meta.jaxb.ForcedType()
                                    .withUserType("com.google.gson.JsonElement")
                                    .withName("varchar")
                                    .withIncludeExpression(".*")
                                    .withIncludeExpression(".*JSON.*")
                                    .withIncludeTypes("JSONB?")
                                    .withName("varchar")
                                    .withIncludeTypes("INET")
                            ).toList()
                        )
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isPojos = true
                        isImmutablePojos = false
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "ru.acuma.k.shuffler"
                        directory = "jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

application {
    applicationDefaultJvmArgs = listOf("---add-opens java.base/java.lang=ALL-UNNAMED")
}
