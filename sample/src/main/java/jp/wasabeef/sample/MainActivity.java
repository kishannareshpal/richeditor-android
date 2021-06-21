package jp.wasabeef.sample;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButtonToggleGroup;
import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity {

  private TextView mPreview;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final RichEditor mEditor = findViewById(R.id.editor);
    final MaterialButtonToggleGroup mbtg_button_group = findViewById(R.id.mbtg_action_group);

    // Configure editor
    mEditor.setEditorHeight(320);
    mEditor.setEditorFontSize(22);
    mEditor.setEditorFontColor(Color.BLACK);
    mEditor.setEditorBackgroundColor(ContextCompat.getColor(this, R.color.editor_background));
    mEditor.setPadding(24, 24, 24, 24);
    //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
    mEditor.setPlaceholder("Insert text here...");
    //mEditor.setInputEnabled(false);



    mEditor.setOnTextSelectionChangeListener((enabledFormatTypes, selectedText) -> {

      for(Map.Entry<String, Boolean> enabledType : enabledFormatTypes.getAllTypes().entrySet()) {
        String formatName = enabledType.getKey();
        boolean isEnabled = enabledType.getValue();


        int buttonId;
        if (formatName.equals("isBold")) {
          buttonId = R.id.action_bold;
        } else if (formatName.equals("isItalic")) {
          buttonId = R.id.action_italic;
        } else if (formatName.equals("isUnderline")) {
          buttonId = R.id.action_underline;
        } else if (formatName.equals("isStrikethrough")) {
          buttonId = R.id.action_strikethrough;
        } else {
          buttonId = View.NO_ID;
        }

        if (isEnabled) {
          mbtg_button_group.check(buttonId);
        } else {
          mbtg_button_group.uncheck(buttonId);
        }
      }
    });

    mbtg_button_group.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
      if (checkedId == R.id.action_bold) {
        mEditor.setBold(isChecked);

      } else if (checkedId == R.id.action_italic) {
        mEditor.setItalic(isChecked);

      } else if (checkedId == R.id.action_underline) {
        mEditor.setUnderline(isChecked);

      } else if (checkedId == R.id.action_strikethrough) {
        mEditor.setStrikeThrough(isChecked);
      }
    });


    findViewById(R.id.action_undo).setOnClickListener(v -> {
      mEditor.undo();
    });


    findViewById(R.id.action_redo).setOnClickListener(v -> mEditor.redo());

//    findViewById(R.id.action_bold).setOnClickListener(v -> mEditor.toggleBold());
//
//    findViewById(R.id.action_italic).setOnClickListener(v -> mEditor.toggleItalic());

    findViewById(R.id.action_subscript).setOnClickListener(v -> mEditor.setSubscript());

    findViewById(R.id.action_superscript).setOnClickListener(v -> mEditor.setSuperscript());

//    findViewById(R.id.action_strikethrough).setOnClickListener(v -> mEditor.setStrikeThrough(true));

//    findViewById(R.id.action_underline).setOnClickListener(v -> mEditor.setUnderline(true));

    findViewById(R.id.action_heading1).setOnClickListener(v -> mEditor.setHeading(1));

    findViewById(R.id.action_heading2).setOnClickListener(v -> mEditor.setHeading(2));

    findViewById(R.id.action_heading3).setOnClickListener(v -> mEditor.setHeading(3));

    findViewById(R.id.action_heading4).setOnClickListener(v -> mEditor.setHeading(4));

    findViewById(R.id.action_heading5).setOnClickListener(v -> mEditor.setHeading(5));

    findViewById(R.id.action_heading6).setOnClickListener(v -> mEditor.setHeading(6));

    findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
      private boolean isChanged;

      @Override public void onClick(View v) {
        mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
        isChanged = !isChanged;
      }
    });

    findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
      private boolean isChanged;

      @Override public void onClick(View v) {
        mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
        isChanged = !isChanged;
      }
    });

    findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.setIndent();
      }
    });

    findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.setOutdent();
      }
    });

    findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.setAlignLeft();
      }
    });

    findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.setAlignCenter();
      }
    });

    findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.setAlignRight();
      }
    });

    findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.setBlockquote();
      }
    });

    findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.setBullets();
      }
    });

    findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.setNumbers();
      }
    });

    findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
            "dachshund");
      }
    });

    findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
      }
    });
    findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mEditor.insertTodo();
      }
    });
  }
}
