import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class CustomAndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("de.mannodermaus.android-junit5")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 32
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {

                add("implementation", libs.findDependency("androidx.core.ktx").get())
                add("implementation", libs.findDependency("koin").get())
                add("implementation", libs.findDependency("kotlinx.coroutines.android").get())
                add("testImplementation", libs.findDependency("junit5Api").get())
                add("testRuntimeOnly", libs.findDependency("junit5Engine").get())
                add("testImplementation", libs.findDependency("mockk").get())
                add("testImplementation", libs.findDependency("kotlinx.coroutines.test").get())
            }
        }
    }

}
