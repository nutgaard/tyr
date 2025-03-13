plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "no.kartverket"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.ktor.serialization.kotlinx.json)

    testImplementation(kotlin("test"))
//    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
//    testImplementation(libs.junit.engine)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}