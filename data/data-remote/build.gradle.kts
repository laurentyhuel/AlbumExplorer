plugins {
    id("albumexplorer.android.library")
    id("kotlinx-serialization")
}

android {
    namespace = "com.lyh.albumexplorer.data.remote"
}

dependencies {
    implementation(libs.retrofit.core)

    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)

    implementation(libs.kotlinx.serialization.json)
}