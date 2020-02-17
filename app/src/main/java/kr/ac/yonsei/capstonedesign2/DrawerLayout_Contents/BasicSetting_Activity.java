package kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.yonsei.capstonedesign2.R;

public class BasicSetting_Activity extends AppCompatActivity implements View.OnClickListener {

    public static String SEEKBAR_SEC = "SEEKBAR_SEC_DATA";
    public static String BASIC_SETTING = "BASIC_SETTING";

    public static String ARMBAND_DIRECTION = "ARM_DIRECTION";
    public static String ARMBAND_RIGHT = "ARM_RIGHT";
    public static String ARMBAND_LEFT = "ARM_LEFT";

    // UI element
    private TextView txt_restTimeValue;
    private RadioGroup radioGroup;
    private RadioButton radioButton_left, radioButton_right;
    private ImageButton btn_maximum, btn_set1, btn_set2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_setting);

        SeekBar seekBar = findViewById(R.id.seekBar_restTime);
        txt_restTimeValue = findViewById(R.id.txt_sec_setvalue);
        radioGroup = findViewById(R.id.radioGroup_armband_direction);
        radioButton_right = findViewById(R.id.radioButton_right);
        radioButton_left = findViewById(R.id.radioButton_left);

        btn_maximum = findViewById(R.id.imgbtn_maximumMuscle);
        btn_set1 = findViewById(R.id.imgbtn_set1);
        btn_set2 = findViewById(R.id.imgbtn_set2);

        btn_maximum.setOnClickListener(this);
        btn_set1.setOnClickListener(this);
        btn_set2.setOnClickListener(this);

        final SharedPreferences sharedPreferences = getSharedPreferences(BASIC_SETTING, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        int secValue = sharedPreferences.getInt(SEEKBAR_SEC, 1);
        txt_restTimeValue.setText(String.valueOf(secValue));
        seekBar.setProgress(secValue);

        // Get RadioButton State and Set Check
        if (sharedPreferences.getString(ARMBAND_DIRECTION, ARMBAND_LEFT).equals(ARMBAND_LEFT)) {
            radioButton_left.setChecked(true);
        }else {
            radioButton_right.setChecked(true);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences sharedPreferences = getSharedPreferences(BASIC_SETTING, MODE_PRIVATE);
                txt_restTimeValue.setText(String.valueOf(seekBar.getProgress()));
                editor.putInt(SEEKBAR_SEC, progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_left:
                        editor.putString(ARMBAND_DIRECTION, ARMBAND_LEFT);
                        editor.apply();
                        break;
                    case R.id.radioButton_right:
                        editor.putString(ARMBAND_DIRECTION, ARMBAND_RIGHT);
                        editor.apply();
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_maximumMuscle:
                Intent intent = new Intent(BasicSetting_Activity.this, MaximumMuscle_Activity.class);
                startActivity(intent);
                break;
            case R.id.imgbtn_set1:
                break;
            case R.id.imgbtn_set2:
                break;
        }
    }
}
