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

public class SettingActivity extends AppCompatActivity {

    SeekBar voiceSpeedSeekBar,voicePitchSeekBar;
    TextView clearCache;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private Switch autoSetBackgroundSwitch;
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

        float voiceSpeed=sharedpreferences.getFloat("SpeedVoice", 1f);
        int index=2;
        if(voiceSpeed==0.5){
            index=1;
        }else if(voiceSpeed==1f){
            index=2;
        }else  if(voiceSpeed==1.5f){
            index=3;
        }else if(voiceSpeed==2f){
            index=4;
        }
        else index=0;

        voiceSpeedSeekBar=(SeekBar)findViewById(R.id.voiceSpeedSeekBar);
        voiceSpeedSeekBar.setProgress(index);
        voiceSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int speedVoiceIndex = 1;
            float speedVoice=1f;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedVoiceIndex = progress;
                if(progress==1){
                    speedVoice=0.5f;
                }else if (progress==2){
                    speedVoice=1f;
                }else if (progress==3){
                    speedVoice=1.5f;
                }
                else if(progress==4){
                    speedVoice=2f;
                }
                else speedVoice=0.2f;
                editor.putFloat("SpeedVoice", speedVoice);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        float voicePitch=sharedpreferences.getFloat("VoicePitch", 1f);

        voicePitchSeekBar=(SeekBar) findViewById(R.id.voicePitchSeekBar);
        voicePitchSeekBar.setProgress((int) (voicePitch*10));
        voicePitchSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float voicePitch=1f;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                voicePitch=progress*0.1f;
                editor.putFloat("VoicePitch", voicePitch);
                editor.commit();
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
                        editor.putFloat("SpeedVoice", 1.0f);
                        editor.putFloat("VoicePitch", 1.0f);
                        editor.putBoolean("deleteAll",false);
                        editor.commit();
                        voiceSpeedSeekBar.setProgress(2);
                        voicePitchSeekBar.setProgress(10);
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
