import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    id("org.springframework.boot") version "2.3.2.RELEASE"
}

apply(plugin = "io.spring.dependency-management")

group = "com.example"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
    implementation("org.springframework.boot", "spring-boot-starter-data-rest")
    implementation("org.springframework.boot", "spring-boot-starter-validation")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    runtimeOnly("com.h2database", "h2")
    runtimeOnly("org.postgresql", "postgresql")
    testImplementation("org.springframework.boot", "spring-boot-starter-test")

    implementation("javax.validation", "validation-api", properties["version.javax.validation"].toString())

    implementation("io.codearte.jfairy", "jfairy", properties["version.jfairy"].toString())
    testImplementation("org.junit.jupiter", "junit-jupiter-api", properties["version.junit"].toString())
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", properties["version.junit"].toString())
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(
            TestLogEvent.PASSED,
            TestLogEvent.FAILED,
            TestLogEvent.SKIPPED
        )
    }
}
