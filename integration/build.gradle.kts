plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

evaluationDependsOn(":kotlinx-benchmark-runtime")

val runtime get() = project(":kotlinx-benchmark-runtime")
val plugin get() = gradle.includedBuild("plugin")

dependencies {
    implementation(gradleTestKit())

    testImplementation(kotlin("test-junit"))
}

tasks.test {
    dependsOn(plugin.task(":publishToBuildLocal"))
    dependsOn(runtime.tasks.getByName("publishToBuildLocal"))

    systemProperty("kotlin_repo_url", rootProject.properties["kotlin_repo_url"])
    systemProperty("plugin-maven-url", plugin.projectDir.resolve("build/maven").absolutePath)
    systemProperty("runtime-maven-url", rootProject.buildDir.resolve("maven").absolutePath)
}
