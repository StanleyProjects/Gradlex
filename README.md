# Gradlex
A few Gradle extensions.

---

## Unstable

> GitHub [0.0.3u-SNAPSHOT](https://github.com/StanleyProjects/Gradlex/releases/tag/0.0.3u-SNAPSHOT) release
>
> ...there should be a link to [Maven](https://central.sonatype.com) here

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
    implementation("com.github.kepocnhh:Gradlex:0.0.3u-SNAPSHOT")
}
```

---
