plugins {
    java
    kotlin("jvm") version "1.6.21"
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("plugin.spring") version "1.8.0"
    kotlin("plugin.jpa") version "1.8.0"
}

group = "ws.exon.ecm.marchlabce"
version = "1.0-SNAPSHOT"

val javaVersion = "1.16"
val kotlinVersion = "1.6.10"


repositories {
    mavenCentral()
}

dependencies {
    implementation((kotlin("stdlib")))
    implementation("org.decimal4j:decimal4j:1.0.3")

    implementation("org.threeten:threeten-extra:1.7.2")
    implementation("net.time4j:time4j-base:5.9.3")
    implementation("net.time4j:time4j-sqlxml:5.9.3")
    implementation("net.time4j:time4j-tzdata:5.0-2022a")

    implementation("javax.interceptor:javax.interceptor-api:1.2.2")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.apache.commons:commons-compress:1.22")
    implementation("org.apache.commons:commons-io:1.3.2")
    implementation("com.opencsv:opencsv:5.7.1")
    implementation("ch.obermuhlner:big-math:2.3.1")
//    implementation( "org.springframework.cloud:spring-cloud-starter-config"
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("org.reflections:reflections:0.9.12")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-json")
//    implementation("org.springframework.amqp:spring-rabbit")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.liquibase:liquibase-core")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

val mainClassName = "ws.exon.cm.market.MArchLabApplication"
val jarName = "march-lab-ce"

springBoot {
    buildInfo()
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar>() {
    enabled = true
    archiveBaseName.set(jarName)
    mainClass.set(mainClassName)
    manifest {
        attributes("Main-Class" to "org.springframework.boot.loader.PropertiesLauncher",
        "Start-Class" to mainClassName)
    }
}

