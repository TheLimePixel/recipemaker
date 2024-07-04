import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "io.github.thelimepixel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.darkrockstudios:mpfilepicker:3.1.0")
    implementation("org.snakeyaml:snakeyaml-engine:2.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "recipemaker"
            packageVersion = "1.0.0"
        }
    }
}
