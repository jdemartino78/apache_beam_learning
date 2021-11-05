plugins {
    java
}

val apacheBeamVersion = "2.29.0"
val junitJupiterVersion = "5.7.2"
val lookerLibVersion = "3.1.0"
val lookerXVersion = "0.0.1"
val autoValueVersion = "1.8.1"
val slf4jVersion = "1.7.30"

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/llooker/looker-sdk")
        credentials(PasswordCredentials::class)
    }
}

dependencies {

    implementation("org.slf4j:slf4j-simple:$slf4jVersion")

    implementation("org.apache.beam:beam-sdks-java-core:$apacheBeamVersion")
    implementation("org.apache.beam:beam-runners-direct-java:$apacheBeamVersion")
    implementation("org.apache.beam:beam-runners-google-cloud-dataflow-java:$apacheBeamVersion")
    implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:$apacheBeamVersion")
    implementation("com.google.looker:lib:$lookerLibVersion")
    implementation("com.google.looker:lookerx:$lookerXVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    compileOnly("com.google.auto.value:auto-value-annotations:$autoValueVersion")
    annotationProcessor("com.google.auto.value:auto-value:$autoValueVersion")
}

tasks.test {
    useJUnitPlatform()
}
