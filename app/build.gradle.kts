plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id("jacoco")
}

android {
    namespace = "com.motoacademy.android.qiet"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.motoacademy.android.qiet"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableUnitTestCoverage = true
            isMinifyEnabled = false
            isDebuggable = true
        }

    }

    lint {
        checkAllWarnings = true  // se verdadeiro, verificar todos os problemas, incluindo aqueles que estão desativados por padrão
        abortOnError = true  // se verdadeiro, interromper a compilação do Gradle se forem encontrados erros
        warningsAsErrors = false  // se verdadeiro, tratar todos os avisos como erros
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    testOptions {
        packaging {
            resources.excludes.add("META-INF/*")
        }
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

//    tasks.register<JacocoReport>("jacocoTestReport") {
//        group = "Reporting"
//        description = "Generate JaCoCo coverage report for the debug build."
//
//        dependsOn("testDebugUnitTest")
//
//        finalizedBy(tasks.named("jacocoCoverageVerification").get())
//
//        reports {
//            xml.required.set(true)
//            html.required.set(true)
//            csv.required.set(false)
//        }
//
//        val fileFilter = listOf(
//            "**/*ScreenKt.class",
//            "**/*Activity.class",
//            "**/*ActivityKt*",
//            "**/*Fragmnt.class",
//            "**/*FragmentKt*",
//            "**/dagger/**",
//            "**/hilt/**",
//            "**/generated/**",
//            "**/*_HiltComponents*",
//            "**/*Dagger*",
//            "**/*Hilt*",
//            "**/*\$*",
//            "**/*_Factory*",
//            "**/*_Impl*",
//            "**/*_MembersInjector*",
//            "**/*_Module*",
//            "**/*_Subcomponent*",
//            "**/*_Component*",
//            "**/theme/**",
//            "**/components/**",
//            "**/navigation/**"
//        )
//
//        val javaClasses = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
//            exclude(fileFilter)
//        }
//
//        val kotlinClasses = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
//            exclude(fileFilter)
//        }
//
//        val sourceDirs = listOf("src/main/java", "src/main/kotlin")
//
//        classDirectories.setFrom(files(javaClasses, kotlinClasses))
//        additionalSourceDirs.setFrom(files(sourceDirs))
//        sourceDirectories.setFrom(files(sourceDirs))
//
//        val execFiles = fileTree("$buildDir") {
//            include("**/*.exec")
//        }
//        executionData.setFrom(execFiles)
//    }
//
//    tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
//
//        // classDirectories e executionData devem ser os mesmos do generateJacocoCoverageReport
//        val reportTask = tasks.named<JacocoReport>("jacocoTestReport").get()
//
//        classDirectories.setFrom(reportTask.classDirectories)
//        executionData.setFrom(reportTask.executionData)
//
//        violationRules {
//            rule {
//                limit {
//                    counter = "LINE"
//                    value = "COVEREDRATIO"
//                    // mínimo de 80% de cobertura
//                    minimum = "0.8".toBigDecimal()
//                }
//            }
//        }
//    }
//
//    tasks.register<TestReport>("unitTests") {
//        description = "Generates a combined test report for all modules"
//        group = "verification"
//
//        // Coleta os resultados de todos os módulos
//        val results = subprojects.mapNotNull { subproject ->
//            subproject.tasks.findByName("testDebugUnitTest")?.let { task ->
//                dependsOn(task)
//                (task as? Test)?.binaryResultsDirectory
//            }
//        }
//
//        testResults.from(results)
//        // Caminho do relatório agregado
//        destinationDirectory.set(layout.buildDirectory.dir("reports/tests/aggregate"))
//    }


}

jacoco {
    toolVersion = "0.8.11"
}

val fileFilter = listOf(
    "**/*ScreenKt.class",
    "**/*Activity.class",
    "**/*ActivityKt*",
    "**/*Fragmnt.class",
    "**/*FragmentKt*",
    "**/dagger/**",
    "**/hilt/**",
    "**/generated/**",
    "**/*_HiltComponents*",
    "**/*Dagger*",
    "**/*Hilt*",
    "**/*\$*",
    "**/*_Factory*",
    "**/*_Impl*",
    "**/*_MembersInjector*",
    "**/*_Module*",
    "**/*_Subcomponent*",
    "**/*_Component*",
    "**/theme/**",
    "**/components/**",
    "**/navigation/**"
)

val debugTree = fileTree(
    mapOf(
        "dir" to "${buildDir}/intermediates/javac/debug",
        "excludes" to fileFilter
    )
)

val kotlinDebugTree = fileTree(
    mapOf(
        "dir" to "${buildDir}/tmp/kotlin-classes/debug",
        "excludes" to fileFilter
    )
)

val mainSrc = files("src/main/java", "src/main/kotlin")

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    sourceDirectories.setFrom(mainSrc)
    classDirectories.setFrom(files(debugTree, kotlinDebugTree))
    executionData.setFrom(fileTree(buildDir) {
        include("**/jacoco/testDebugUnitTest.exec")
    })
}

tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
    dependsOn("testDebugUnitTest")

    sourceDirectories.setFrom(mainSrc)
    classDirectories.setFrom(files(debugTree, kotlinDebugTree))
    executionData.setFrom(fileTree(buildDir) {
        include("**/jacoco/testDebugUnitTest.exec")
    })

    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.80".toBigDecimal() // require at least 80% line coverage
            }
        }
    }
}

// hook into `check` so CI/CD fails if coverage is low
tasks.named("check") {
    dependsOn("jacocoTestReport", "jacocoCoverageVerification")
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.kotlinx)
    implementation(libs.kotlinx.serialization.json)

    // Hilt Navigation for Compose
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // AndroidX & Compose

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Testes com coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    // Turbine: testar Flows de forma simples
    testImplementation("app.cash.turbine:turbine:1.1.0")

}