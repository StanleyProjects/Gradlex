repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots")
}

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.github.kepocnhh:Gradlex:0.2.1u-SNAPSHOT")
}
