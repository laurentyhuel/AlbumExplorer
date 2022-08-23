import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class CustomAndroidInstrumentedTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                defaultConfig.testInstrumentationRunnerArguments["runnerBuilder"] =
                    "de.mannodermaus.junit5.AndroidJUnit5Builder"
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {

                add(
                    "androidTestImplementation",
                    libs.findDependency("kotlinx.coroutines.test").get()
                )
                add("androidTestImplementation", libs.findDependency("junit5Api").get())
                add("androidTestImplementation", libs.findDependency("junit5Engine").get())
                add("androidTestImplementation", libs.findDependency("androidx.test.ext").get())
                add("androidTestImplementation", libs.findDependency("androidx.test.runner").get())
                add("androidTestImplementation", libs.findDependency("mannodermaus.core").get())
                add("androidTestRuntimeOnly", libs.findDependency("mannodermaus.runner").get())
            }
        }
    }
}
