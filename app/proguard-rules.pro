# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep Room entities
-keep class com.tronsguitar.ebookeditor.data.local.database.entity.** { *; }

# Keep Hilt generated classes
-keepclasseswithmembers class * {
    @dagger.hilt.* <fields>;
}

# Keep Jetpack Compose
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
