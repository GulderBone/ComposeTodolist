import com.android.build.api.dsl.ApplicationExtension
import com.gulderbone.configure
import com.gulderbone.configureDetekt
import com.gulderbone.configureKotlinAndroid
import com.gulderbone.getLib
import com.gulderbone.getVersion
import com.gulderbone.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
            apply("io.gitlab.arturbosch.detekt")
            apply("jacoco")
        }

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            configureDetekt()
            defaultConfig.targetSdk = target.getVersionCatalog().getVersion("targetSdk").toInt()
        }

        with(dependencies) {
            add("detektPlugins", getVersionCatalog().getLib("detekt"))
        }
    }
}
