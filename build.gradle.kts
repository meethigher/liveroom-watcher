import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.5.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.15.0"
}

group = "top.meethigher"
version = "1.0"

/**
* https://stackoverflow.com/questions/69599536/gradle-sync-error-ssl-peer-shut-down-incorrectly
*/
repositories {
    maven("https://s01.oss.sonatype.org/content/groups/public/")
    mavenCentral()
}

dependencies {
    implementation("top.meethigher:light-repo:1.0")
    implementation("top.meethigher:light-retry:1.0")
    implementation("top.meethigher:cache-store:1.2")
    implementation("top.meethigher:light-statemachine:1.1")
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.alibaba.fastjson2:fastjson2:2.0.40")
    implementation("org.jodd:jodd-http:6.3.0")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}