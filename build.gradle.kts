plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

val springDocVersion = "2.5.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.liquibase:liquibase-core")
	implementation("org.postgresql:postgresql:42.7.3")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.1")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
