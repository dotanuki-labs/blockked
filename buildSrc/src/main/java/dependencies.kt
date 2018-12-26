// Versions for project parameters and dependencies

internal object Kotlin {
    const val version = "1.3.11"
}

internal object Versions {

    const val kotlinxSerialization = "0.9.1"
    const val androidGradlePlugin = "3.3.0-rc03"
    const val testLogger = "1.4.0"
    const val rxJava = "2.2.4"
    const val rxKotlin = "2.2.0"
    const val rxAndroid = "2.1.0"
    const val okhttp = "3.12.0"
    const val retrofit = "2.5.0"
    const val retrofitKotlinxConverter = "0.2.0"
    const val supportLibrary = "1.0.0"
    const val aacLifecycle = "2.0.0"
    const val mpAndroidChart = "v3.0.3"
    const val groupie = "2.3.0"
    const val jUnit4 = "4.12"
    const val burster = "0.1.1"
    const val assertJ = "2.9.1"
    const val mockitoKotlin = "2.0.0-RC3"
    const val mockitoDexMaker = "2.19.0"
    const val androidJUnit = "1.1.0"
    const val espressoCore = "3.1.0"
    const val tite = "0.1.1"
    const val kodeinDI = "6.0.1"
    const val slf4j = "1.7.25"

}

object Dependencies {

    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Kotlin.version}"
    val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kotlinxSerialization}"

    val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"

    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    val okhttpLogger = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val retrofitKTXConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.retrofitKotlinxConverter}"

    val appCompat = "androidx.appcompat:appcompat:${Versions.supportLibrary}"
    val cardView = "androidx.cardview:cardview:${Versions.supportLibrary}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.supportLibrary}"
    val materialDesign = "com.google.android.material:material:${Versions.supportLibrary}"

    val aacLifecycle = "androidx.lifecycle:lifecycle-common:${Versions.aacLifecycle}"
    val aacLifecycleJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.aacLifecycle}"

    val mpAndroidChart = "com.github.PhilJay:MPAndroidChart:${Versions.mpAndroidChart}"
    val groupie = "com.xwray:groupie:${Versions.groupie}"
    val groupieKTX = "com.xwray:groupie-kotlin-android-extensions:${Versions.groupie}"
    val kodein = "org.kodein.di:kodein-di-generic-jvm:${Versions.kodeinDI}"
    val kodeinConf = "org.kodein.di:kodein-di-conf-jvm:${Versions.kodeinDI}"
    val kodeinAndroid = "org.kodein.di:kodein-di-framework-android-x:${Versions.kodeinDI}"

}

object TestDependencies {

    val jUnit = "junit:junit:${Versions.jUnit4}"
    val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Kotlin.version}"
    val tite = "com.github.ubiratansoares:tite:${Versions.tite}"
    val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
    val burster = "com.github.ubiratansoares:burster:${Versions.burster}"
    val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    val mockitoDexMaker = "com.linkedin.dexmaker:dexmaker-mockito:${Versions.mockitoDexMaker}"
    val androidTestCore = "androidx.test:core:${Versions.androidJUnit}"
    val androidTestCoreKtx = "androidx.test:core-ktx:${Versions.androidJUnit}"
    val androidTestExtJunit = "androidx.test.ext:junit:${Versions.androidJUnit}"
    val androidTestExtJunitKtx = "androidx.test.ext:junit-ktx:${Versions.androidJUnit}"
    val androidTestRunner = "androidx.test:runner:${Versions.androidJUnit}"
    val androidTestRules = "androidx.test:rules:${Versions.androidJUnit}"
    val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
    val slf4jNoOp = "org.slf4j:slf4j-nop:${Versions.slf4j}"

}


object BuildPlugins {

    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
    val kotlinxSerializiationPlugin = "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
    val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    val testLoggerPlugin = "com.adarshr:gradle-test-logger-plugin:${Versions.testLogger}"

}


object AndroidModule {

    val main = listOf(
        Dependencies.kotlinStdlib,
        Dependencies.rxJava,
        Dependencies.rxKotlin,
        Dependencies.rxAndroid,
        Dependencies.appCompat,
        Dependencies.cardView,
        Dependencies.recyclerView,
        Dependencies.materialDesign,
        Dependencies.aacLifecycle,
        Dependencies.aacLifecycleJava8,
        Dependencies.kodein,
        Dependencies.kodeinAndroid,
        Dependencies.kodeinConf
    )

    val unitTesting = listOf(
        TestDependencies.slf4jNoOp,
        TestDependencies.jUnit,
        TestDependencies.burster,
        TestDependencies.assertJ,
        TestDependencies.kotlinReflect,
        TestDependencies.mockitoKotlin,
        TestDependencies.tite
    )

    val androidTesting = listOf(
        TestDependencies.slf4jNoOp,
        TestDependencies.assertJ,
        TestDependencies.androidTestRunner,
        TestDependencies.androidTestRules,
        TestDependencies.androidTestCore,
        TestDependencies.androidTestCoreKtx,
        TestDependencies.androidTestExtJunit,
        TestDependencies.androidTestExtJunitKtx,
        TestDependencies.espresso,
        TestDependencies.kotlinReflect,
        TestDependencies.mockitoKotlin,
        TestDependencies.mockitoDexMaker
    )
}

object StandaloneModule {

    val main = listOf(
        Dependencies.kotlinStdlib,
        Dependencies.rxJava,
        Dependencies.rxKotlin,
        Dependencies.kodein
    )

    val unitTesting = listOf(
        TestDependencies.jUnit,
        TestDependencies.assertJ,
        TestDependencies.burster,
        TestDependencies.slf4jNoOp,
        TestDependencies.mockitoKotlin,
        TestDependencies.kotlinReflect,
        TestDependencies.tite
    )

}