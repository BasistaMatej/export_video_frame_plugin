# This is only adaptation of export_video_frame plugin for flutter!

Because export_video_frame was not usable in the year 2024 for adnroid platform, this version is (partially) modified so that the basic functionality of this plugin works.
There was no change with the iOS version.

# Export Video frame for Flutter

A Flutter plugin for iOS and Android for exporting picture from video file.

## Installation

add

```
dependencies:
  flutter:
    sdk: flutter

  export_video_frame:
    git:
      url: git@github.com:BasistaMatej/export_video_frame_plugin.git
      ref: main

```

as a dependency in your pubspec.yaml file.

If you use saveAblum api,you need add the add the following keys to your Info.plist file, located ios/Runner/Info.plist:

```xml

<key>NSPhotoLibraryUsageDescription</key>
<string>Use Ablum For your purpose</string>

```

### Android

Make sure you add the needed permissions to your Android Manifest Permission.

```gradle
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

[Example Demo](https://pub.dev/packages/export_video_frame#-example-tab-)
