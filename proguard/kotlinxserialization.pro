-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class io.dotanuki.service.blockchaininfo.**$$serializer { *; }
-keepclassmembers class io.dotanuki.service.blockchaininfo.** {
    *** Companion;
}
-keepclasseswithmembers class io.dotanuki.service.blockchaininfo.** {
    kotlinx.serialization.KSerializer serializer(...);
}