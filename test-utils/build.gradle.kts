plugins {
    kotlin("jvm")
}

group = "no.kartverket"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":kabac"))
    implementation(libs.logback.classic)
    implementation(libs.junit.api)
    implementation(libs.logback.classic)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}