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
    TextView clearCache,valueSeekBarSpeed;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private Switch autoSpeakSwitch,autoSetBackgroundSwitch;
    private boolean autoBackground=false;
    SharedPreferences.Editor editor;


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
        final boolean autoBackground=sharedpreferences.getBoolean("autoBackground", false);
        autoSetBackgroundSwitch = (Switch) findViewById(R.id.autoSetBackgroundSwitch);
        autoSetBackgroundSwitch.setChecked(autoBackground);
        editor = sharedpreferences.edit();
        autoSetBackgroundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor.putBoolean("autoBackground",true);
                }else{
                    editor.putBoolean("autoBackground",false);
                }
                editor.commit();
            }
        });

        int voiceSpeed=sharedpreferences.getInt("SpeedVoice", 1600);
        int index=2;
        if(voiceSpeed==11025){
            index=1;
        }else if(voiceSpeed==16000){
            index=2;
        }else  if(voiceSpeed==22050){
            index=3;
        }else {
            index=4;
        }

        translateDBHelper = new TranslateDBHelper(this);
        voiceSpeedSeekBar=(SeekBar)findViewById(R.id.voiceSpeedSeekBar);
        voiceSpeedSeekBar.setProgress(index);
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
        clearCache=(TextView) findViewById(R.id.clearCache);
        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete this?");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        editor.putBoolean("autoBackground",false);
                        editor.putInt("SpeedVoice", 16000);
                        editor.putBoolean("deleteAll",false);
                        editor.commit();
                       voiceSpeedSeekBar.setProgress(2);
                        autoSetBackgroundSwitch.setChecked(false);

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
