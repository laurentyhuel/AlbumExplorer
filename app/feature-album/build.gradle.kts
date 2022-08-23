plugins {
    id("albumexplorer.android.library")
}

android {
    namespace = "com.lyh.albumexplorer.feature.album"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":app:feature-core"))

    implementation(libs.koin.android)

    implementation(libs.timber)
    implementation(libs.coil.kt)
    implementation(libs.androidx.appcompat)
    implementation(libs.material3)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    testImplementation(libs.turbine)
}