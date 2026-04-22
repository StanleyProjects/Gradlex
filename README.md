# Gradlex
A few Gradle extensions.

---

## Release

`0.1.0`
| [GitHub](https://github.com/StanleyProjects/Gradlex/releases/tag/0.1.0)
| [Maven](https://central.sonatype.com/artifact/com.github.kepocnhh/Gradlex/0.1.0)
| [Docs](https://StanleyProjects.github.io/Gradlex/docs/0.1.0)

### Build
```
$ gradle lib:assembleReleaseJar
```

### Import
```kotlin
dependencies {
    implementation("com.github.kepocnhh:Gradlex:0.1.0")
}
```

---

## Snapshot

> GitHub [0.2.1-SNAPSHOT](https://github.com/StanleyProjects/Gradlex/releases/tag/0.2.1-SNAPSHOT) release
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
    implementation("com.github.kepocnhh:Gradlex:0.2.1-SNAPSHOT")
}
```

---

## Unstable

> GitHub [0.2.1u-SNAPSHOT](https://github.com/StanleyProjects/Gradlex/releases/tag/0.2.1u-SNAPSHOT) release
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
    implementation("com.github.kepocnhh:Gradlex:0.2.1u-SNAPSHOT")
}
```

---
