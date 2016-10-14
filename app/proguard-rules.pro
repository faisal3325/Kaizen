# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }


-keep class com.mmi.apis.distance.** {
    <fields>;
    <methods>;
}

-keep class com.mmi.apis.place.geocoder.** {
    <fields>;
    <methods>;
}

-keep class com.mmi.apis.place.reversegeocode.** {
    <fields>;
    <methods>;
}

-keep class com.mmi.apis.place.** {
    <fields>;
    <methods>;
}



-keep class com.mmi.apis.routing.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.apis.place.autosuggest.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.apis.place.details.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.apis.place.nearby.** {
    <fields>;
    <methods>;
}
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
