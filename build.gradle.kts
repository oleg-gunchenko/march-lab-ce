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

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.projectlombok:lombok:1.18.18")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.hibernate:hibernate-jpamodelgen:5.4.12.Final")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")


    testImplementation(("org.junit.jupiter:junit-jupiter-api:5.6.0"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs.plus("-Xjsr305=strict")
    }
}
