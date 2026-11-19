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


include(":uniqueidgenerator")
project(":uniqueidgenerator").projectDir = File("../uniqueidgenerator")

include(":stringcodec")
project(":stringcodec").projectDir = File("../../stringcodec")
