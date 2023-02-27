@file:Suppress("UnstableApiUsage")
rootProject.name = "shuffler"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()

        maven {
            name = "shuffler-catalog"
            url = uri("https://maven.pkg.github.com/QAcuma/shuffler-catalog")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    versionCatalogs {
        create("libs") {
            from("ru.acuma:shuffler-catalog:2.0.3")
        }
    }
}
