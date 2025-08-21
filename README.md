# Gradlex
A few Gradle extensions.

---

## Unstable

> GitHub [0.0.6u-SNAPSHOT](https://github.com/StanleyProjects/Gradlex/releases/tag/0.0.6u-SNAPSHOT) release
> 
> Maven [metadata](https://central.sonatype.com/repository/maven-snapshots/com/github/kepocnhh/Gradlex/maven-metadata.xml)

### Build
```
$ gradle lib:assembleUnstableJar
```

### Import
```kotlin
repositories {
    maven("https://central.sonatype.com/repository/maven-snapshots")
}

dependencies {
    implementation("com.github.kepocnhh:Gradlex:0.0.6u-SNAPSHOT")
}
```

---
