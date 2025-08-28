# Gradlex
A few Gradle extensions.

---

## Snapshot

> GitHub [0.1.0-SNAPSHOT](https://github.com/StanleyProjects/Gradlex/releases/tag/0.1.0-SNAPSHOT) release
>
> Maven [metadata](https://central.sonatype.com/repository/maven-snapshots/com/github/kepocnhh/Gradlex/maven-metadata.xml)

### Build
```
$ gradle lib:assembleSnapshotJar
```

### Import
```kotlin
repositories {
    maven("https://central.sonatype.com/repository/maven-snapshots")
}

dependencies {
    implementation("com.github.kepocnhh:Gradlex:0.1.0-SNAPSHOT")
}
```

---

## Unstable

> GitHub [0.1.0u-SNAPSHOT](https://github.com/StanleyProjects/Gradlex/releases/tag/0.1.0u-SNAPSHOT) release
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
    implementation("com.github.kepocnhh:Gradlex:0.1.0u-SNAPSHOT")
}
```

---
