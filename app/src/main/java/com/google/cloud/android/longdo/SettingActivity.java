package com.google.cloud.android.longdo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.cloud.android.longdo.sqlites.TranslateDBHelper;

public class SettingActivity extends AppCompatActivity {

    private static final int[] SAMPLE_RATE_CANDIDATES = new int[]{11025, 16000, 22050, 44100};
    private TranslateDBHelper translateDBHelper;
    SeekBar voiceSpeedSeekBar,voicePitchSeekBar;
    TextView clearTranslateTextView,valueSeekBarSpeed;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private Switch autoSpeakSwitch,autoSetBackgroundSwitch;
    private boolean autoSpeak=false,autoSetBG=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSettingActivity);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Settings");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        boolean autoSpeak=sharedpreferences.getBoolean("autoSpeak", false);
        autoSpeakSwitch = (Switch) findViewById(R.id.autoSpeakSwitch);
        autoSpeakSwitch.setChecked(autoSpeak);
        autoSpeakSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor1 = sharedpreferences.edit();
                if(isChecked){
                    editor1.putBoolean("autoSpeak",true);
                }else{
                    editor1.putBoolean("autoSpeak",false);
                }
                editor1.commit();
            }
        });
        SharedPreferences.Editor editor2 = sharedpreferences.edit();
        editor2.putBoolean("autoSpeak",autoSpeak);

        valueSeekBarSpeed=(TextView) findViewById(R.id.valueSeekBarSpeed);
        translateDBHelper = new TranslateDBHelper(this);
        voiceSpeedSeekBar=(SeekBar)findViewById(R.id.voiceSpeedSeekBar);
        voiceSpeedSeekBar.setProgress(2);
        voiceSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int speedVoiceIndex = 1;
            int speedVoice=16000;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedVoiceIndex = progress;
                if(progress==1){
                    speedVoice=11025;
                }else if (progress==2){
                    speedVoice=16000;
                }else if (progress==3){
                    speedVoice=22050;
                }
                else speedVoice=44100;
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("SpeedVoice", speedVoice);
                editor.commit();

//                valueSeekBarSpeed.setText(String.valueOf(speedVoice)+"Hz");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        voicePitchSeekBar=(SeekBar) findViewById(R.id.voicePitchSeekBar);
        voicePitchSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        clearTranslateTextView=(TextView) findViewById(R.id.clearTranslateTextView);
        clearTranslateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete this?");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        translateDBHelper.deleteAllTranslates();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
