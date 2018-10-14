# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/jakub/Android/Sdk/tools/proguard/proguard-android.txt
# You can editSuccess the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; }

-dontwarn kotlin.reflect.**
-keep class kotlin.** { *; }

-keep class okio.** { *; }
-dontwarn okio.**

-dontwarn java.lang.invoke.*

-keep class org.joda.time.DateTime { *; }
-keep class org.joda.time.Seconds { *; }
-keep class org.joda.time.Duration { *; }
-dontwarn org.joda.time.**
-dontwarn org.joda.convert.**

-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-keep class com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

-keep class android.support.v7.widget.** { *; }

-keep class no.byggemappen.mvp.** { *; }
-keep class * extends com.google.gson.JsonDeserializer
-keep class * extends com.google.gson.JsonSerializer

-dontwarn com.google.**
-dontwarn sun.misc.**
-dontwarn org.objenesis.instantiator.sun.**

-dontwarn org.conscrypt.Conscrypt
