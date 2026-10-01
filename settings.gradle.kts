pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "o2omap"

include(":generickeyvalueregistry")
project(":generickeyvalueregistry").projectDir = File("../generic_key_value_registry")

include(":uniqueidgenerator")
project(":uniqueidgenerator").projectDir = File("../uniqueidgenerator")
