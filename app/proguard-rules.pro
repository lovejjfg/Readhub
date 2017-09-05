# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/joezhang/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# okhttp
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault


#retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
#-dontwarn retrofit2.OkHttpCall
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn javax.annotation.**
# keep anotation
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

#rx
#-dontwarn rx.**

# lambda
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

-keepattributes InnerClasses
-dontwarn InnerClasses
-dontwarn InnerClasses$*


-dontwarn com.lovejjfg.**

-keepattributes EnclosingMethod



