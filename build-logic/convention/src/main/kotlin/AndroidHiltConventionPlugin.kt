import com.gulderbone.getLib
import com.gulderbone.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidHiltConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.kapt")
            apply("dagger.hilt.android.plugin")
        }

        with(dependencies) {
            add("implementation", getVersionCatalog().getLib("hilt-android"))
            add("kapt", getVersionCatalog().getLib("hilt-compiler"))
        }
    }
}
