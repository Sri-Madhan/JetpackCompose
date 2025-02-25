pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        gradlePluginPortal()
    }
    plugins{
        id("de.jensklingenberg.ktorfit") version "2.2.0"
        id("org.jetbrains.kotlin.android") version "2.1.0" apply false
        id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Compose"
include(":app")
 