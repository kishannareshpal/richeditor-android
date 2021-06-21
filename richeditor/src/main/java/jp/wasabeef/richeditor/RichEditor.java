package jp.wasabeef.richeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Copyright (C) 2017 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class RichEditor extends WebView {

  public class EnabledFormatTypes {
    private boolean isBold;
    private boolean isItalic;
    private boolean isUnderline;
    private boolean isStrikethrough;
    private boolean isSuperscript;
    private boolean isSubscript;

    public EnabledFormatTypes(boolean isBold, boolean isItalic, boolean isUnderline, boolean isStrikethrough, boolean isSuperscript, boolean isSubscript) {
      this.isBold = isBold;
      this.isItalic = isItalic;
      this.isUnderline = isUnderline;
      this.isStrikethrough = isStrikethrough;
      this.isSuperscript = isSuperscript;
      this.isSubscript = isSubscript;
    }

    public boolean isBold() {
      return isBold;
    }
    public boolean isItalic() {
      return isItalic;
    }
    public boolean isUnderline() {
      return isUnderline;
    }
    public boolean isStrikethrough() {
      return isStrikethrough;
    }
    public boolean isSuperscript() {
      return isSuperscript;
    }
    public boolean isSubscript() {
      return isSubscript;
    }

    public HashMap<String, Boolean> getEnabledTypesOnly() {
      HashMap<String, Boolean> enabledMap = new HashMap<>();
      if (isBold)          enabledMap.put("isBold", isBold);
      if (isItalic)        enabledMap.put("isItalic", isItalic);
      if (isUnderline)     enabledMap.put("isUnderline", isUnderline);
      if (isStrikethrough) enabledMap.put("isStrikethrough", isStrikethrough);
      if (isSuperscript)   enabledMap.put("isSuperscript", isSuperscript);
      if (isSubscript)     enabledMap.put("isSubscript", isSubscript);
      return enabledMap;
    }

    public HashMap<String, Boolean> getAllTypes() {
      HashMap<String, Boolean> enabledMap = new HashMap<>();
      enabledMap.put("isBold", isBold);
      enabledMap.put("isItalic", isItalic);
      enabledMap.put("isUnderline", isUnderline);
      enabledMap.put("isStrikethrough", isStrikethrough);
      enabledMap.put("isSuperscript", isSuperscript);
      enabledMap.put("isSubscript", isSubscript);
      return enabledMap;
    }

  }

  public enum Type {
    BOLD,
    ITALIC,
    SUBSCRIPT,
    SUPERSCRIPT,
    STRIKETHROUGH,
    UNDERLINE,
    H1,
    H2,
    H3,
    H4,
    H5,
    H6,
    ORDEREDLIST,
    UNORDEREDLIST,
    JUSTIFYCENTER,
    JUSTIFYFULL,
    JUSTIFYLEFT,
    JUSTIFYRIGHT
  }

  public interface OnTextChangeListener {
    void onTextChange(String text, String html);
  }

  public interface OnTextSelectionChangeListener {
    void onTextSelect(EnabledFormatTypes enabledFormatTypes, String selectedText);
  }

  public interface OnDecorationStateListener {
    void onStateChangeListener(String text, List<Type> types);
  }

  public interface AfterInitialLoadListener {
    void onAfterInitialLoad(boolean isReady);
  }

  public class JavascriptInterface {
    private Context ctx;

    /** Instantiate the interface and set the context */
    JavascriptInterface(Context ctx) {
      this.ctx = ctx;
    }

    /** Methods to be called from javascript page */
    @android.webkit.JavascriptInterface
    public void textChange(String text, String html) {
      RichEditor.this.textChange(text, html);
    }

    @android.webkit.JavascriptInterface
    public void selectionChange(String enabledFormatsAsQueryString, String selectedText) {
      RichEditor.this.selectionChange(enabledFormatsAsQueryString, selectedText);
    }


  }

  private static final String SETUP_HTML = "file:///android_asset/editor.html";
  private static final String STATE_SCHEME = "re-state://";
  private boolean isReady = false;
  private int textSize = 14;
  private String text; // the text written in the editor
  private String html; // the html equivalent of the text written in the editor
  private OnTextChangeListener mTextChangeListener;
  private OnTextSelectionChangeListener mTextSelectionChangeListener;
  private AfterInitialLoadListener mLoadListener;

  public RichEditor(Context context) {
    this(context, null);
  }

  public RichEditor(Context context, AttributeSet attrs) {
    this(context, attrs, android.R.attr.webViewStyle);
  }

  @SuppressLint("SetJavaScriptEnabled")
  public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    setVerticalScrollBarEnabled(false);
    setHorizontalScrollBarEnabled(false);
    getSettings().setJavaScriptEnabled(true);
    setWebChromeClient(new WebChromeClient());
    setWebViewClient(createWebviewClient());
    addJavascriptInterface(new JavascriptInterface(context), "Android");
    loadUrl(SETUP_HTML);
    applyAttributes(context, attrs);
  }

  protected EditorWebViewClient createWebviewClient() {
    return new EditorWebViewClient();
  }

  public void setOnTextChangeListener(OnTextChangeListener listener) {
    this.mTextChangeListener = listener;
  }

  public void setTextSize(int textSize) {
    this.textSize = textSize;
  }

  public void setOnTextSelectionChangeListener(OnTextSelectionChangeListener listener) {
    this.mTextSelectionChangeListener = listener;
  }

  public void setOnInitialLoadListener(AfterInitialLoadListener listener) {
    this.mLoadListener = listener;
  }

  private void textChange(String text, String html) {
    this.text = text;
    this.html = html;
    if (mTextChangeListener != null) {
      mTextChangeListener.onTextChange(text, html);
    }
  }

  private void selectionChange(String queryStringWithEnabledFormats, String selectedText) {
    Uri uri = Uri.parse(queryStringWithEnabledFormats); // parse the query string and retrieve its values
    boolean isBold          = uri.getBooleanQueryParameter("isBold",          false);
    boolean isItalic        = uri.getBooleanQueryParameter("isItalic",        false);
    boolean isSubscript     = uri.getBooleanQueryParameter("isSubscript",     false);
    boolean isUnderline     = uri.getBooleanQueryParameter("isUnderline",     false);
    boolean isSuperscript   = uri.getBooleanQueryParameter("isSuperscript",   false);
    boolean isStrikethrough = uri.getBooleanQueryParameter("isStrikethrough", false);

    // create an object
    EnabledFormatTypes eft = new EnabledFormatTypes(isBold, isItalic, isUnderline, isStrikethrough, isSuperscript, isSubscript);
    if (mTextSelectionChangeListener != null) {
      mTextSelectionChangeListener.onTextSelect(eft, selectedText);
    }
  }

  @SuppressLint("RtlHardcoded")
  private void applyAttributes(Context context, AttributeSet attrs) {
    final int[] attrsArray = new int[] {
        android.R.attr.gravity
    };
    TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

    int gravity = ta.getInt(0, NO_ID);
    switch (gravity) {
      case Gravity.LEFT:
      case Gravity.START:
        exec("javascript:RE.setTextAlign(\"left\")");
        break;
      case Gravity.RIGHT:
      case Gravity.END:
        exec("javascript:RE.setTextAlign(\"right\")");
        break;
      case Gravity.TOP:
        exec("javascript:RE.setVerticalAlign(\"top\")");
        break;
      case Gravity.BOTTOM:
        exec("javascript:RE.setVerticalAlign(\"bottom\")");
        break;
      case Gravity.CENTER_VERTICAL:
        exec("javascript:RE.setVerticalAlign(\"middle\")");
        break;
      case Gravity.CENTER_HORIZONTAL:
        exec("javascript:RE.setTextAlign(\"center\")");
        break;
      case Gravity.CENTER:
        exec("javascript:RE.setVerticalAlign(\"middle\")");
        exec("javascript:RE.setTextAlign(\"center\")");
        break;
    }

    ta.recycle();
  }

  public void setHtml(String contents) {
    if (contents == null) {
      contents = "";
    }
    try {
      exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
    } catch (UnsupportedEncodingException e) {
      // No handling
    }
    this.html = contents;
  }

  public String getHtml() {
    return this.html;
  }

  public String getText() {
    return this.text;
  }

  public void setEditorFontColor(int color) {
    String hex = convertHexColorString(color);
    exec("javascript:RE.setBaseTextColor('" + hex + "');");
  }

  public void setEditorFontSize(int px) {
    exec("javascript:RE.setBaseFontSize('" + px + "px');");
  }

  @Override public void setPadding(int left, int top, int right, int bottom) {
    super.setPadding(left, top, right, bottom);
    exec("javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
        + "px');");
  }

  @Override public void setPaddingRelative(int start, int top, int end, int bottom) {
    // still not support RTL.
    setPadding(start, top, end, bottom);
  }

  public void setEditorBackgroundColor(int color) {
    setBackgroundColor(color);
  }

  @Override public void setBackgroundColor(int color) {
    super.setBackgroundColor(color);
  }

  @Override public void setBackgroundResource(int resid) {
    Bitmap bitmap = Utils.decodeResource(getContext(), resid);
    String base64 = Utils.toBase64(bitmap);
    bitmap.recycle();

    exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
  }

  @Override public void setBackground(Drawable background) {
    Bitmap bitmap = Utils.toBitmap(background);
    String base64 = Utils.toBase64(bitmap);
    bitmap.recycle();

    exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
  }

  public void setBackground(String url) {
    exec("javascript:RE.setBackgroundImage('url(" + url + ")');");
  }

  public void setEditorWidth(int px) {
    exec("javascript:RE.setWidth('" + px + "px');");
  }

  public void setEditorHeight(int px) {
    exec("javascript:RE.setHeight('" + px + "px');");
  }

  public void setPlaceholder(String placeholder) {
    exec("javascript:RE.setPlaceholder('" + placeholder + "');");
  }

  public void setInputEnabled(Boolean inputEnabled) {
    exec("javascript:RE.setInputEnabled(" + inputEnabled + ")");
  }

  public void loadCSS(String cssFile) {
    String jsCSSImport = "(function() {" +
        "    var head  = document.getElementsByTagName(\"head\")[0];" +
        "    var link  = document.createElement(\"link\");" +
        "    link.rel  = \"stylesheet\";" +
        "    link.type = \"text/css\";" +
        "    link.href = \"" + cssFile + "\";" +
        "    link.media = \"all\";" +
        "    head.appendChild(link);" +
        "}) ();";
    exec("javascript:" + jsCSSImport + "");
  }

  public void undo() {
    exec("javascript:RE.undo();");
  }

  public void redo() {
    exec("javascript:RE.redo();");
  }

  public void toggleBold() {
    exec("javascript:RE.toggleBold();");
  }

  public void setBold(boolean enabled) {
    exec("javascript:RE.setBold(" + enabled + ");");
  }

  public void toggleItalic() {
    exec("javascript:RE.toggleItalic();");
  }

  public void setItalic(boolean enabled) {
    exec("javascript:RE.setItalic(" + enabled + ");");
  }

  public void setSubscript() {
    exec("javascript:RE.setSubscript();");
  }

  public void setSuperscript() {
    exec("javascript:RE.setSuperscript();");
  }

  public void toggleStrikeThrough() {
    exec("javascript:RE.toggleStrikeThrough();");
  }

  public void setStrikeThrough(boolean enabled) {
    exec("javascript:RE.setStrikeThrough(" + enabled + ");");
  }

  public void toggleUnderline() {
    exec("javascript:RE.toggleUnderline();");
  }

  public void setUnderline(boolean enabled) {
    exec("javascript:RE.setUnderline(" + enabled + ");");
  }

  public void setTextColor(int color) {
    exec("javascript:RE.prepareInsert();");

    String hex = convertHexColorString(color);
    exec("javascript:RE.setTextColor('" + hex + "');");
  }

  public void setTextBackgroundColor(int color) {
    exec("javascript:RE.prepareInsert();");

    String hex = convertHexColorString(color);
    exec("javascript:RE.setTextBackgroundColor('" + hex + "');");
  }

  public void setFontSize(int fontSize) {
    if (fontSize > 7 || fontSize < 1) {
      Log.e("RichEditor", "Font size should have a value between 1-7");
    }
    exec("javascript:RE.setFontSize('" + fontSize + "');");
  }

  public void removeFormat() {
    exec("javascript:RE.removeFormat();");
  }

  public void setHeading(int heading) {
    exec("javascript:RE.setHeading('" + heading + "');");
  }

  public void setIndent() {
    exec("javascript:RE.setIndent();");
  }

  public void setOutdent() {
    exec("javascript:RE.setOutdent();");
  }

  public void setAlignLeft() {
    exec("javascript:RE.setJustifyLeft();");
  }

  public void setAlignCenter() {
    exec("javascript:RE.setJustifyCenter();");
  }

  public void setAlignRight() {
    exec("javascript:RE.setJustifyRight();");
  }

  public void setBlockquote() {
    exec("javascript:RE.setBlockquote();");
  }

  public void setBullets() {
    exec("javascript:RE.setBullets();");
  }

  public void setNumbers() {
    exec("javascript:RE.setNumbers();");
  }

  public void insertImage(String url, String alt) {
    exec("javascript:RE.prepareInsert();");
    exec("javascript:RE.insertImage('" + url + "', '" + alt + "');");
  }

  public void insertLink(String href, String title) {
    exec("javascript:RE.prepareInsert();");
    exec("javascript:RE.insertLink('" + href + "', '" + title + "');");
  }

  public void insertTodo() {
    exec("javascript:RE.prepareInsert();");
    exec("javascript:RE.setTodo('" + Utils.getCurrentTime() + "');");
  }

  public void focusEditor() {
    requestFocus();
    exec("javascript:RE.focus();");
  }

  public void clearFocusEditor() {
    exec("javascript:RE.blurFocus();");
  }

  private String convertHexColorString(int color) {
    return String.format("#%06X", (0xFFFFFF & color));
  }

  protected void exec(final String trigger) {
    this.post(new Runnable() {
      @Override
      public void run() {
        load(trigger);
      }
    });

//    if (isReady) {
//
//    } else {
//      postDelayed(new Runnable() {
//        @Override public void run() {
//          exec(trigger);
//        }
//      }, 100);
//    }
  }

  private void load(String trigger) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      evaluateJavascript(trigger, null);
    } else {
      loadUrl(trigger);
    }
  }

  protected class EditorWebViewClient extends WebViewClient {
    @Override public void onPageFinished(WebView view, String url) {
      isReady = url.equalsIgnoreCase(SETUP_HTML);
      if (mLoadListener != null) {
        mLoadListener.onAfterInitialLoad(isReady);
      }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      String decode;
      try {
        decode = URLDecoder.decode(url, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        // No handling
        return false;
      }

      if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
        return true;
      }

      return super.shouldOverrideUrlLoading(view, url);
    }
  }
}
