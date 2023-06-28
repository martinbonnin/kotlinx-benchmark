plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

evaluationDependsOn(":kotlinx-benchmark-runtime")

val runtime get() = project(":kotlinx-benchmark-runtime")
val plugin get() = gradle.includedBuild("plugin")

val createClasspathManifest by tasks.registering {
    dependsOn(plugin.task(":publishToBuildLocal"))
    dependsOn(runtime.tasks.getByName("publishToBuildLocal"))

    val outputDir = file("$buildDir/$name")
    outputs.dir(outputDir)
    doLast {
        outputDir.apply {
            mkdirs()
            resolve("plugin-maven-url.txt").writeText(plugin.projectDir.resolve("build/maven").absolutePath)
            resolve("runtime-maven-url.txt").writeText(rootProject.buildDir.resolve("maven").absolutePath)
        }
    }
}

dependencies {
    implementation(files(createClasspathManifest))
    implementation(gradleTestKit())

    testImplementation(kotlin("test-junit"))
}

tasks.test {
    systemProperty("kotlin_repo_url", rootProject.properties["kotlin_repo_url"])
}
