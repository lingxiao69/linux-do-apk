# Linux Do WebView App

将 [linux.do](https://linux.do) 网站打包成 Android APK 的轻量级应用。

## ✨ 特性

- 🚀 **快速加载** - 优先使用缓存，秒开常用页面
- 📦 **体积小** - Release 版本启用 R8 压缩
- 🎯 **顶部进度条** - 蓝色进度条显示加载状态
- 🔄 **下拉刷新** - 支持下拉刷新页面
- 🔐 **登录持久化** - Cookie 持久化保存登录状态
- ↩️ **返回导航** - 返回键浏览网页历史

## 📱 编译 APK

### 前置要求

- [Android Studio](https://developer.android.com/studio) 2023.1+
- JDK 17+

### 编译步骤

1. 克隆或下载此项目
2. 用 Android Studio 打开项目目录
3. 等待 Gradle 同步完成
4. **Build** → **Generate App Bundles or APKs** → **Build APK(s)**

APK 输出路径：`app/build/outputs/apk/debug/LinuxDo.apk`

### 签名 Release 版本

1. **Build** → **Generate Signed App Bundle / APK...**
2. 选择 APK，创建或选择签名密钥
3. 选择 release，勾选 V1 和 V2 签名
4. 点击 Finish

## 📂 项目结构

```
app/src/main/
├── java/com/linuxdo/app/
│   └── MainActivity.kt      # WebView 主逻辑
├── res/
│   ├── layout/              # 布局文件
│   ├── drawable/            # 进度条样式
│   ├── mipmap-*/            # 应用图标
│   └── values/              # 字符串、主题、颜色
└── AndroidManifest.xml      # 权限声明
```

## ⚙️ 自定义

修改 `MainActivity.kt` 中的 `targetUrl` 可更换目标网站：

```kotlin
private val targetUrl = "https://linux.do"
```

## 📄 开源协议

MIT License

## 系统要求

Android 7.0+ (API 24)
