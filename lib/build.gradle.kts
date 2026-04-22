import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import sp.kx.gradlex.GitHub
import sp.kx.gradlex.Markdown
import sp.kx.gradlex.Maven
import sp.kx.gradlex.asFile
import sp.kx.gradlex.assemble
import sp.kx.gradlex.buildDir
import sp.kx.gradlex.buildSrc
import sp.kx.gradlex.check
import sp.kx.gradlex.dir
import sp.kx.gradlex.eff
import sp.kx.gradlex.ufc
import java.net.URI

version = "0.2.1"

val maven = Maven.Artifact(
    group = "com.github.kepocnhh",
    id = rootProject.name,
)

val gh = GitHub.Repository(
    owner = "StanleyProjects",
    name = rootProject.name,
)

val developer = Maven.Developer(
    name = "Stanley Wintergreen",
    url = URI("https://github.com/kepocnhh"),
)

repositories.mavenCentral()

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.gradle.jacoco")
    id("io.gitlab.arturbosch.detekt") version Version.detekt
    id("org.jetbrains.dokka") version Version.dokka
}

tasks.getByName<JavaCompile>("compileJava") {
    targetCompatibility = Version.jvmTarget
}

val compileKotlinTask = tasks.getByName<KotlinCompile>("compileKotlin") {
    kotlinOptions {
        jvmTarget = Version.jvmTarget
        freeCompilerArgs += setOf("-module-name", maven.moduleName(separator = '-'))
    }
}

tasks.getByName<JavaCompile>("compileTestJava") {
    targetCompatibility = Version.jvmTarget
}

tasks.getByName<KotlinCompile>("compileTestKotlin") {
    kotlinOptions.jvmTarget = Version.jvmTarget
}

dependencies {
    if (Version.gradle != gradle.gradleVersion) error("Gradle version: ${gradle.gradleVersion}!")
    implementation(gradleApi())
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Version.jupiter}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Version.jupiter}")
}

fun Test.getExecutionData(): File {
    return buildDir()
        .dir("jacoco")
        .asFile("$name.exec")
}

val taskUnitTest: Test = tasks.register<Test>("checkUnitTest") {
    useJUnitPlatform()
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED") // https://github.com/gradle/gradle/issues/18647
    doLast {
        getExecutionData().eff()
    }
}.get()

jacoco.toolVersion = Version.jacoco

val taskCoverageReport: JacocoReport = tasks.register<JacocoReport>("assembleCoverageReport") {
    dependsOn(taskUnitTest)
    reports {
        csv.required = false
        html.required = true
        xml.required = false
    }
    sourceDirectories.setFrom(file("src/main/kotlin"))
    classDirectories.setFrom(sourceSets.main.get().output.classesDirs)
    executionData(taskUnitTest.getExecutionData())
    doLast {
        val report = buildDir()
            .dir("reports/jacoco/$name/html")
            .eff("index.html")
        println("Coverage report: ${report.absolutePath}")
    }
}.get()

tasks.register<JacocoCoverageVerification>("checkCoverage") {
    dependsOn(taskCoverageReport)
    violationRules {
        rule {
            limit {
                minimum = BigDecimal(0.90) // todo 96
            }
        }
    }
    classDirectories.setFrom(taskCoverageReport.classDirectories)
    executionData(taskCoverageReport.executionData)
}

tasks.register<Detekt>("checkCodeQuality") {
    buildUponDefaultConfig = true
    allRules = true
    jvmTarget = Version.jvmTarget
    val sourceSet = sourceSets.getByName("main")
    source = sourceSet.allSource
    val configs = setOf(buildSrc.dir("src/main/resources/detekt").eff("config.yml"))
    config.setFrom(configs)
    val report = buildDir()
        .dir("reports/analysis/code/quality/${sourceSet.name}/html")
        .asFile("index.html")
    reports {
        html {
            required = true
            outputLocation = report
        }
        md.required = false
        sarif.required = false
        txt.required = false
        xml.required = false
    }
    val detektTask = tasks.getByName<Detekt>("detekt${sourceSet.name.ufc()}")
    classpath.setFrom(detektTask.classpath)
    doFirst {
        println("Analysis report: ${report.absolutePath}")
    }
}

tasks.register<Detekt>("checkDocs") {
    buildUponDefaultConfig = false
    allRules = false
    jvmTarget = Version.jvmTarget
    val sourceSet = sourceSets.getByName("main")
    source = sourceSet.allSource
    val configs = setOf(buildSrc.dir("src/main/resources/detekt").eff("docs.yml"))
    config.setFrom(configs)
    val report = buildDir()
        .dir("reports/analysis/docs/html")
        .asFile("index.html")
    reports {
        html {
            required = true
            outputLocation = report
        }
        md.required = false
        sarif.required = false
        txt.required = false
        xml.required = false
    }
    val detektTask = tasks.getByName<Detekt>("detekt${sourceSet.name.ufc()}")
    classpath.setFrom(detektTask.classpath)
    doFirst {
        println("Analysis report: ${report.absolutePath}")
    }
}

fun tasks(variant: String, version: String, maven: Maven.Artifact, gh: GitHub.Repository) {
    tasks.register("assemble${variant.ufc()}MavenMetadata") {
        doLast {
            val target = buildDir().dir("yml").file("maven-metadata.yml")
            val file = maven.assemble(version = version, target = target)
            println("Maven metadata: ${file.absolutePath}")
        }
    }
    tasks.register<Jar>("assemble${variant.ufc()}Jar") {
        dependsOn(compileKotlinTask)
        archiveBaseName = maven.id
        archiveVersion = version
        from(compileKotlinTask.destinationDirectory.asFileTree)
    }
    tasks.register<Jar>("assemble${variant.ufc()}Source") {
        archiveBaseName = maven.id
        archiveVersion = version
        archiveClassifier = "sources"
        from(sourceSets.main.get().allSource)
    }
    tasks.register<Jar>("assemble${variant.ufc()}Metadata") {
        doLast {
            val target = buildDir().dir("yml").file("metadata.yml")
            val file = gh.assemble(version = version, target = target)
            println("Metadata: ${file.absolutePath}")
        }
    }
}

"unstable".also { variant ->
    val version = "${version}u-SNAPSHOT"
    tasks(variant = variant, version = version, maven = maven, gh = gh)
    tasks.register("assemble${variant.ufc()}Pom") {
        doLast {
            val target = buildDir().dir("libs").file("${maven.name(version = version)}.pom")
            val text = maven.pom(version = version, packaging = "jar")
            val file = target.assemble(text = text)
            println("POM: ${file.absolutePath}")
        }
    }
    tasks.register("check${variant.ufc()}Readme") {
        doLast {
            val expected = setOf(
                "GitHub ${Markdown.link(text = version, uri = gh.release(version = version))}",
                "Maven ${Markdown.link("metadata", Maven.Snapshot.metadata(artifact = maven))}",
                "maven(\"${Maven.Snapshot.Host}\")",
                "implementation(\"${maven.moduleName(version = version)}\")",
                "gradle lib:assemble${variant.replaceFirstChar(Char::titlecase)}Jar",
            )
            rootDir.resolve("README.md").check(
                expected = expected,
                report = buildDir()
                    .dir("reports/analysis/readme")
                    .asFile("index.html"),
            )
        }
    }
}

"snapshot".also { variant ->
    val version = "$version-SNAPSHOT"
    tasks(variant = variant, version = version, maven = maven, gh = gh)
    tasks.register("assemble${variant.ufc()}Pom") {
        doLast {
            val target = buildDir().dir("libs").file("${maven.name(version = version)}.pom")
            val text = maven.pom(version = version, packaging = "jar")
            val file = target.assemble(text = text)
            println("POM: ${file.absolutePath}")
        }
    }
    tasks.register("check${variant.ufc()}Readme") {
        doLast {
            val expected = setOf(
                "GitHub ${Markdown.link(text = version, uri = gh.release(version = version))}",
                "Maven ${Markdown.link("metadata", Maven.Snapshot.metadata(artifact = maven))}",
                "maven(\"${Maven.Snapshot.Host}\")",
                "implementation(\"${maven.moduleName(version = version)}\")",
                "gradle lib:assemble${variant.replaceFirstChar(Char::titlecase)}Jar",
            )
            rootDir.resolve("README.md").check(
                expected = expected,
                report = buildDir()
                    .dir("reports/analysis/readme")
                    .asFile("index.html"),
            )
        }
    }
}

"release".also { variant ->
    val version = version.toString()
    tasks(variant = variant, version = version, maven = maven, gh = gh)
    tasks.register("assemble${variant.ufc()}Pom") {
        doLast {
            val target = buildDir().dir("libs").file("${maven.name(version = version)}.pom")
            val license = gh.uri("blob/$version/LICENSE")
            val text = maven.pom(
                version = version,
                packaging = "jar",
                uri = gh.uri(),
                licenses = setOf(license),
                scm = gh.uri(),
                tag = version,
                developers = setOf(developer),
            )
            val file = target.assemble(text = text)
            println("POM: ${file.absolutePath}")
        }
    }
    tasks.register("check${variant.ufc()}Readme") {
        doLast {
            val expected = setOf(
                Markdown.link(text = "GitHub", uri = gh.release(version = version)),
                Markdown.link(text = "Maven", uri = maven.uri(version = version)),
                Markdown.link(text = "Docs", uri = gh.pages("docs/$version")),
                "implementation(\"${maven.moduleName(version = version)}\")",
                "gradle lib:assemble${variant.replaceFirstChar(Char::titlecase)}Jar",
            )
            rootDir.resolve("README.md").check(
                expected = expected,
                report = buildDir()
                    .dir("reports/analysis/readme")
                    .asFile("index.html"),
            )
        }
    }
    val docsTask = tasks.register<DokkaTask>("assemble${variant.ufc()}Docs") {
        outputDirectory = buildDir().dir("docs/$variant")
        moduleName = gh.name
        moduleVersion = version
        dokkaSourceSets.getByName("main") {
            val path = "src/$name/kotlin"
            reportUndocumented = false
            sourceLink {
                localDirectory = file(path)
                remoteUrl = gh.uri("tree/${moduleVersion.get()}/lib/$path").toURL()
            }
            jdkVersion = Version.jvmTarget.toInt()
        }
        doLast {
            val index = outputDirectory.get().eff("index.html")
            println("Docs: ${index.absolutePath}")
        }
    }.get()
    tasks.register<Jar>("assemble${variant.ufc()}Javadoc") {
        dependsOn(docsTask)
        archiveBaseName = maven.id
        archiveVersion = version
        archiveClassifier = "javadoc"
        from(docsTask.outputDirectory)
    }
}
