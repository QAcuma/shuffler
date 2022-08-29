rootProject.name = "shuffler"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenLocal()
    }

    versionCatalogs {
        create("libs") {
            from("ru.acuma:shuffler-catalog:1.0.5")
        }
    }
}
