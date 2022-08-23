plugins {
    id("albumexplorer.android.library")
}

android {
    namespace = "com.lyh.albumexplorer.domain"
}

dependencies {
    testImplementation(libs.turbine)
}