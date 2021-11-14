@file:Suppress("MemberVisibilityCanBePrivate")

import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

fun PluginDependenciesSpec.shadow(module: String): PluginDependencySpec = id(module)

object Shadow {
    const val version = "7.1.0"
    const val id = "com.github.johnrengelman.shadow"
}
object Jvm {
    const val version = "1.8"
}

object Kotlin {
    const val version = "1.5.31"
    const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    const val jvmId = "jvm"
    const val kaptId = "kapt"
}

object JNA {
    const val version = "5.9.0"
    const val core = "net.java.dev.jna:jna:$version"
    const val platform = "net.java.dev.jna:jna-platform:$version"
}

object Logger {
    const val version = "2.14.1"
    const val core = "org.apache.logging.log4j:log4j-core:$version"
    const val api = "org.apache.logging.log4j:log4j-api:$version"
    const val slf4j = "org.apache.logging.log4j:log4j-slf4j-impl:$version"
}

object Manifests {
    const val version = "1.1"
    const val core = "com.jcabi:jcabi-manifests:$version"
}

object FlatLookAndFeel {
    const val version = "1.6.1"
    const val core = "com.formdev:flatlaf:$version"
}
