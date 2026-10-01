plugins {
    id("com.android.library")
}

group = "com.trodevel.o2omap"
version = "1.0.0"

android {
    namespace = "com.trodevel.o2omap"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    sourceSets {
        getByName("main") {
            java.setSrcDirs(listOf("src/main/kotlin"))
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation(project(":generickeyvalueregistry"))
    implementation(project(":uniqueidgenerator"))
    testImplementation("junit:junit:4.13.2")
}
