pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AlbumExplorer"

include(":app")
include(":data:data-remote")
include(":data:data-local")
include(":data")
include(":domain")
include(":app:feature-album")
include(":app:feature-core")
