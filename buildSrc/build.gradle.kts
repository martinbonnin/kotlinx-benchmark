import org.jetbrains.kotlin.gradle.plugin.*
import java.util.*

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    mavenLocal()
}

kotlinDslPluginOptions {
    //experimentalWarning.set(false)
}