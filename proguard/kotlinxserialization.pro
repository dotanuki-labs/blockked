-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class io.dotanuki.services.common.**$$serializer { *; }
-keepclassmembers class io.dotanuki.services.common.** {
    *** Companion;
}
-keepclasseswithmembers class io.dotanuki.services.common.** {
    kotlinx.serialization.KSerializer serializer(...);
}