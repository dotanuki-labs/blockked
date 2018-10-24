// Versions for project parameters and dependencies

internal object Kotlin {
    const val version = "1.2.71"
}

internal object Versions {

    const val kotlinxSerialize = "0.5.1"
    const val androidGradlePlugin = "3.2.1"
    const val testLogger = "1.4.0"

    const val rxJava2 = "2.1.15"
    const val rxKotlin2 = "2.2.0"
    const val rxAndroid2 = "2.0.1"
    const val okhttp3 = "3.10.0"
    const val retrofit2 = "2.4.0"
    const val ktxConverter = "0.0.1"
    const val supportLibrary = "28.0.0"
    const val aac = "1.1.1"
    const val jUnit4 = "4.12"
    const val bursterTesting = "0.1.0"
    const val assertJ = "2.9.1"
    const val mockitoKT = "2.0.0-RC1"
    const val mockitoDexMaker2 = "2.19.0"
    const val androidJUnit = "1.0.2"
    const val espressoCore = "3.0.2"
    const val roboletric = "3.8"
    const val barista2 = "2.7.0"
    const val rxEspressoIdler = "0.9.0"
    const val tite = "0.1.0"
    const val kodeinDI = "5.2.0"
    const val slf4j = "1.7.25"

}

object Dependencies {

    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Kotlin.version}"
    val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kotlinxSerialize}"

    val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava2}"
    val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin2}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid2}"

    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp3}"
    val okhttpInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp3}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit2}"
    val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit2}"
    val retrofitKTXConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.ktxConverter}"

    val appCompat = "com.android.support:appcompat-v7:${Versions.supportLibrary}"


    val kodein = "org.kodein.di:kodein-di-generic-jvm:${Versions.kodeinDI}"
    val kodeinConf = "org.kodein.di:kodein-di-conf-jvm:${Versions.kodeinDI}"

}

object TestDependencies {

    val jUnit = "junit:junit:${Versions.jUnit4}"
    val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Kotlin.version}"
    val tite = "com.github.ubiratansoares:tite:${Versions.tite}"
    val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
    val burster = "com.github.ubiratansoares:burster:${Versions.bursterTesting}"
    val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKT}"
    val mockitoDexMaker = "com.linkedin.dexmaker:dexmaker-mockito:${Versions.mockitoDexMaker2}"
    val androidTestRunner = "com.android.support.test:runner:${Versions.androidJUnit}"
    val espresso = "com.android.support.test.espresso:espresso-core:${Versions.espressoCore}"
    val roboletric = "org.robolectric:robolectric:${Versions.roboletric}"
    val rxIdler = "com.squareup.rx.idler:rx2-idler:${Versions.rxEspressoIdler}"
    val barista = "com.schibsted.spain:barista:${Versions.barista2}"
    val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp3}"
    val slf4jNoOp = "org.slf4j:slf4j-nop:${Versions.slf4j}"


}


object BuildPlugins {

    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
    val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    val kotlinxSerializiationPlugin = "org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin:${Versions.kotlinxSerialize}"
    val testLoggerPlugin = "com.adarshr:gradle-test-logger-plugin:${Versions.testLogger}"

}


object AndroidModule {

    val main = listOf(
        Dependencies.kotlinStdlib,
        Dependencies.rxJava,
        Dependencies.rxKotlin,
        Dependencies.rxAndroid,
        Dependencies.appCompat,
        Dependencies.kodein,
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
        TestDependencies.barista,
        TestDependencies.rxIdler,
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
        Dependencies.kodein,
        Dependencies.kodeinConf
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