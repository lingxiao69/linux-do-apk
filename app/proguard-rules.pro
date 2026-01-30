# WebView 优化规则

# 保留 WebView JavaScript 接口
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# 保留 WebView
-keepclassmembers class android.webkit.WebView {
    public *;
}

# 移除日志
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# 优化
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
