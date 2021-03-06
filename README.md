# RichEditor for Android

### Modifications made by @kishannareshpal:

```
    - Custom default font (jost.ttf)
        To override it for a font family of your own, do as follow:
            a. Create the /assets folder on your app package if you haven't already;
            b. Create a subfolder named "fonts" inside your /assets directory.
            c. Place your custom font .ttf file inside the fonts directory.
            d. Make sure that the font file is named: "refont.ttf"
            e. Enjoy!

    - Migrated to AndroidX
    - Upgrade of Android Gradle.
    – Bumped up the minSdkVersion from 14 to 17, due to JavascriptInterfaces method.
    – Implemented a different, more reliable, approach of passing data between WebView and JS. Before: Callback URL Scheme. Now: JavascriptInterfaces
    – Removed OnDecorationStateListener as i found it unnecessary.

    – Added OnTextSelectionChangeListener which contains two params, one is an object that has all of the information about what format type is enabled to the current Caret; And the second param is the actual selected text.
    – Modified OnTextChangeListener, it now returns the html and the text.

    - Changed `.setBold()` to `.setBold(boolean enabled)`
    - Changed `.setItalic()` to `.setItalic(boolean enabled)`
    - Changed `.setUnderline()` to `.setUnderline(boolean enabled)`
    - Changed `.setStrikeThrough()` to `.setStrikethrough(boolean enabled)`

    - Added `.toggleBold()`
    - Added `.toggleItalic()`
    - Added `.toggleUnderline()`
    - Added `.toggleStrikeThrough()`
```

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-richeditor--android-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1696)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/wasabeef/maven/richeditor-android/images/download.svg)](https://bintray.com/wasabeef/maven/richeditor-android/_latestVersion)

`RichEditor for Android` is a beautiful Rich Text `WYSIWYG Editor` for `Android`.

-   _Looking for iOS? Check out_ [cjwirth/RichEditorView](https://github.com/cjwirth/RichEditorView)

## Supported Functions

![Toolbar](./art/demo.gif)

-   [x] Bold
-   [x] Italic
-   [x] Subscript
-   [x] Superscript
-   [x] Strikethrough
-   [x] Underline
-   [x] Justify Left
-   [x] Justify Center
-   [x] Justify Right
-   [x] Blockquote
-   [x] Heading 1
-   [x] Heading 2
-   [x] Heading 3
-   [x] Heading 4
-   [x] Heading 5
-   [x] Heading 6
-   [x] Undo
-   [x] Redo
-   [x] Indent
-   [x] Outdent
-   [x] Insert Image
-   [x] Insert Link
-   [x] Checkbox
-   [x] Text Color
-   [x] Text Background Color
-   [x] Text Font Size
-   [x] Unordered List (Bullets)
-   [x] Ordered List (Numbers)
-   [x] Font Family

## Attribute change of editor

-   [x] Font Size
-   [x] Background Color
-   [x] Width
-   [x] Height
-   [x] Placeholder
-   [x] Load CSS
-   [x] State Callback

## Demo

![Demo](./art/demo2.gif)

## Samples

<a href="https://play.google.com/store/apps/details?id=jp.wasabeef.sample"><img src="http://www.android.com/images/brand/get_it_on_play_logo_large.png"/></a>

## How do I use it?

### Setup

##### Gradle

```groovy
repositories {
    jcenter()
    // maven { url 'https://jitpack.io' } // uncomment this if you are using kishan's build.
}

dependencies {
    implementation 'jp.wasabeef:richeditor-android:1.2.2'

    // or use (for kishan's edit, and upgrades listed on top of this readme file.)
    implementation 'com.github.kishannareshpal:richeditor-android:1.4.3'
    // don't forget to add jitpack repo by uncommenting the line above.

}
```

### Default Setting for Editor

---

**Height**

```java
editor.setEditorHeight(200);
```

**Font**

```java
editor.setEditorFontSize(22);
editor.setEditorFontColor(Color.RED);
```

**Background**

```java
editor.setEditorBackgroundColor(Color.BLUE);
editor.setBackgroundColor(Color.BLUE);
editor.setBackgroundResource(R.drawable.bg);
editor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
```

**Padding**

```java
editor.setPadding(10, 10, 10, 10);
```

**Placeholder**

```java
editor.setPlaceholder("Insert text here...");
```

**Others**  
Please refer the [samples](https://github.com/wasabeef/richeditor-android/blob/master/sample/src/main/java/jp/wasabeef/sample/MainActivity.java) for usage.

### Functions for ContentEditable

---

**Bold**

```java
// Enable bold
editor.setBold(true);
// Disable bold
editor.setBold(false);
// Toggle bold
editor.toggleBold();
```

**Italic**

```java
// Enable italic
editor.setItalic(true);
// Disable italic
editor.setItalic(false);
// Toggle italic
editor.toggleItalic();
```

**Underline**

```java
// Enable underline
editor.setUnderline(true);
// Disable italic
editor.setUnderline(false);
// Toggle italic
editor.toggleUnderline();
```

**StrikeThrough**

```java
// Enable strikethrough
editor.setStrikeThrough(true);
// Disable strikethrough
editor.setStrikeThrough(false);
// Toggle strikethrough
editor.toggleStrikeThrough();
```

**Insert Image**

```java
editor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/twitter.png","twitter");
```

**Text Change Listener**

```java
RichEditor editor = (RichEditor) findViewById(R.id.editor);
editor. setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
  @Override
  public void onTextChange(String text) {
    // Do Something
     Log.d("RichEditor", "Preview " + text);
  }
});
```

**Others**  
Please refer the [samples](https://github.com/wasabeef/richeditor-android/blob/master/sample/src/main/java/jp/wasabeef/sample/MainActivity.java) for usage.

## Requirements

Android SDK 17+

## Developed By

-   Daichi Furiya (wasabeef) - <dadadada.chop@gmail.com>
-   Kishan Jadav (kishannareshpal) - <kishan_jadav@hotmail.com>

-   Inspired by `ZSSRichTextEditor` by [nnhubbard](https://github.com/nnhubbard/ZSSRichTextEditor).

## License

    Copyright 2021 Daichi Furya and Kishan Jadav

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
