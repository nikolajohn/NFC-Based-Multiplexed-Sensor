package com.example.administrator.wrongtry.activities.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.administrator.wrongtry.R;

import static com.example.administrator.wrongtry.activities.activities.MainActivity.demo;

public class SramTestActivity extends AppCompatActivity {

    private static CheckBox lcdCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sram_test);

        lcdCheck = findViewById(R.id.MyLCD_checkbox);
        lcdCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    Log.d("zhulihang","Checked");
                } else {
                    Log.d("zhulihang","Not Checked");
                }
            }
        });

        demo.MyLED();

    }

    public static boolean MyLcdEnabled() {
        return lcdCheck.isChecked();
    }
}
