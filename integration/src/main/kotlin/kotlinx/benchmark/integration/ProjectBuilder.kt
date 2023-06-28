package kotlinx.benchmark.integration

import kotlin.io.path.readText
import kotlin.io.path.toPath

class ProjectBuilder {
    private val configurations = mutableMapOf<String, BenchmarkConfiguration>()
    private val targets = mutableMapOf<String, BenchmarkTarget>()

    fun configuration(name: String, configuration: BenchmarkConfiguration.() -> Unit = {}) {
        configurations[name] = BenchmarkConfiguration().apply(configuration)
    }
    fun register(name: String, configuration: BenchmarkTarget.() -> Unit = {}) {
        targets[name] = BenchmarkTarget().apply(configuration)
    }

    fun build(original: String): String {

        val script =
            """
benchmark {
    configurations {
        ${configurations.flatMap { it.value.lines(it.key) }.joinToString("\n        ")}
    }
    targets {
        ${targets.flatMap { it.value.lines(it.key) }.joinToString("\n        ")}
    }
}
            """.trimIndent()

        return buildScript + "\n\n" + original + "\n\n" + script
    }
}

private val buildScript = run {
    """
    buildscript {
        repositories {
            mavenCentral()
            maven { url '${readText("plugin-maven-url.txt")}' }
        }
        dependencies {
            classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21'
            classpath 'org.jetbrains.kotlinx:kotlinx-benchmark-plugin:0.4.255-SNAPSHOT'
        }
    }
    
    apply plugin: 'kotlin-multiplatform'
    apply plugin: 'org.jetbrains.kotlinx.benchmark'
    
    repositories {
        mavenCentral()
        maven { url '${readText("runtime-maven-url.txt")}' }
    }
    """.trimIndent()
}

private fun readText(fileName: String): String {
    val resource = ProjectBuilder::class.java.classLoader.getResource(fileName)
        ?: throw IllegalStateException("Could not find resource '$fileName'")
    return resource.toURI().toPath().readText()
}
