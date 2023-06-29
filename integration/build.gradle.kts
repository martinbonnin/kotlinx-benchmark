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
    dependsOn(plugin.task(":publishToMavenLocal"))
    dependsOn(runtime.tasks.getByName("publishToMavenLocal"))
    systemProperty("kotlin_repo_url", rootProject.properties["kotlin_repo_url"])
}
