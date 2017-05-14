/*
 * Copyright 2016 Google Inc. All Rights Reserved.
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

package com.google.cloud.android.longdo;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.android.longdo.adapters.ListLanguageAdapter;
import com.google.cloud.android.longdo.adapters.TranslateAdapter;
import com.google.cloud.android.longdo.adapters.TranslateAdapter2;
import com.google.cloud.android.longdo.models.Language;
import com.google.cloud.android.longdo.models.Translate;
import com.google.cloud.android.longdo.responses.TranslateResponse;
import com.google.cloud.android.longdo.services.WebserviceUtil;
import com.google.cloud.android.longdo.sqlites.LanguageDBHelper;
import com.google.cloud.android.longdo.sqlites.TranslateDBHelper;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.cloud.android.longdo.SettingActivity.MyPREFERENCES;


public class MainActivity extends AppCompatActivity implements MessageDialogFragment.Listener {

    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private SpeechService mSpeechService;
    private static final String API_KEY = "AIzaSyCIWb579dBtIL5BflFopri9L1OB1Md8Wsk";
    private VoiceRecorder mVoiceRecorder;
    private TranslateDBHelper translateDBHelper;
    private LanguageDBHelper languageDBHelper;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor1;
    int voiceSpeed;
    private FloatingActionButton fabListen;
    private RecyclerView mRecyclerView;
    String resourceLanguageCode="en",targetLanguageCode="ja",speechCodeSource="en-US",speechCodeTarget="ja-JP",flagFrom="us",flagTo="jp",languageFrom="English (United States)",languageTo="Japanese";
    TranslateAdapter adapter;
    TranslateAdapter2 adapter2;
    ArrayList<Translate> array_list;
    private TextView targetLanguage,sourceLanguage;
    private ImageView flagIconFrom,flagIconTo;
    TextToSpeech t1;



    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            if (mSpeechService != null) {
                voiceSpeed=sharedpreferences.getInt("SpeedVoice", 16000);
                Log.d("GetSamplerate",String.valueOf(voiceSpeed));
                mSpeechService.startRecognizing(voiceSpeed);
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (mSpeechService != null) {
                mSpeechService.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            if (mSpeechService != null) {
                mSpeechService.finishRecognizing();
            }
        }

    };



    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mSpeechService = SpeechService.from(binder);
            mSpeechService.addListener(mSpeechServiceListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mSpeechService = null;
        }

    };


    @Override
    protected void onResume(){
        super.onResume();
        voiceSpeed=sharedpreferences.getInt("SpeedVoice", 16000);
        boolean autoBackground=sharedpreferences.getBoolean("autoBackground", false);
        Log.d("Delete",String.valueOf(voiceSpeed));
        if(autoBackground){
            adapter2= new TranslateAdapter2(array_list, getApplication());
            mRecyclerView.setAdapter(adapter2);
            Log.d("autoBackground","adapter2");
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        }
        else {
            adapter = new TranslateAdapter(array_list, getApplication());
            mRecyclerView.setAdapter(adapter);
            Log.d("autoBackground", "adapter");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setTitle("");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor1 = sharedpreferences.edit();
        boolean autoBackground=sharedpreferences.getBoolean("autoBackground", false);
        flagIconFrom=(ImageView) findViewById(R.id.toolBarFlagFrom);
        flagIconFrom.setBackgroundResource(R.drawable.us);
        flagIconTo=(ImageView) findViewById(R.id.toolBarFlagTo);
        flagIconTo.setBackgroundResource(R.drawable.jp);
        sourceLanguage=(TextView) findViewById(R.id.sourceLanguage);
        sourceLanguage.setOnClickListener(onClickListener);
        targetLanguage=(TextView) findViewById(R.id.targetLanguage);
        targetLanguage.setOnClickListener(onClickListener);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });



        fabListen= (FloatingActionButton) findViewById(R.id.btnFloatingAction);
        fabListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SpeechService.class);
                intent.putExtra("speechCode", speechCodeSource);
                // Prepare Cloud Speech API
                bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
                startVoiceRecorder();

                Log.d("Log","Listening");
                fabListen.animate().scaleX(0).scaleY(0).start();
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        translateDBHelper = new TranslateDBHelper(this);
        languageDBHelper=new LanguageDBHelper(this);
        array_list = translateDBHelper.getAllTranslates();
        if (array_list!=null){
            mRecyclerView.setBackground(null);
        }
        Log.d("autoBackground",String.valueOf(autoBackground));
        if(autoBackground){
        adapter2= new TranslateAdapter2(array_list, getApplication());
            mRecyclerView.setAdapter(adapter2);
            Log.d("autoBackground","adapter2");
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
        adapter = new TranslateAdapter(array_list, getApplication());
            mRecyclerView.setAdapter(adapter);
            Log.d("autoBackground","adapter");
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }


    }
    @Override

    public boolean onOptionsItemSelected(final MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent setting = new Intent(this, SettingActivity.class);
            startActivity(setting);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
        if(id==R.id.action_deleteAllTranslates){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Confirm Delete...");
            alertDialog.setMessage("Are you sure you want delete this?");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    translateDBHelper.deleteAllTranslates();
                    mRecyclerView.setAdapter(new TranslateAdapter(translateDBHelper.getAllTranslates(),getApplication()));
                    mRecyclerView.invalidate();
                    mRecyclerView.smoothScrollToPosition(0);
                    mRecyclerView.setBackgroundResource(R.drawable.backgroud_main);
                    RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                    itemAnimator.setAddDuration(1000);
                    itemAnimator.setRemoveDuration(1000);
                    mRecyclerView.setItemAnimator(itemAnimator);
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog.show();

        }
        if(id==R.id.action_about){
            Intent aboutActivity = new Intent(this, AboutActivity.class);
            startActivity(aboutActivity);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(id==R.id.action_recommend){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.google.android.apps.translate"));
            startActivity(intent);
        }
        if(id==R.id.action_feedback){
            Intent feedbackActivity = new Intent(this,FeedbackActivity.class);
            startActivity(feedbackActivity);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(getApplication(), SpeechService.class);
        intent.putExtra("speechCode", speechCodeSource);
        // Prepare Cloud Speech API
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }

    }


    @Override
    protected void onStop() {
        // Stop listening to voice
        stopVoiceRecorder();
        // Stop Cloud Speech API
        mSpeechService.removeListener(mSpeechServiceListener);
        unbindService(mServiceConnection);
        mSpeechService = null;
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecorder();
            } else {
                showPermissionMessageDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    private void showPermissionMessageDialog() {
        MessageDialogFragment
                .newInstance(getString(R.string.permission_message))
                .show(getSupportFragmentManager(), FRAGMENT_MESSAGE_DIALOG);
    }


    @Override
    public void onMessageDialogDismissed() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    private final SpeechService.Listener mSpeechServiceListener =
            new SpeechService.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    if (isFinal) {
                        mVoiceRecorder.dismiss();
                    }
                    if (!TextUtils.isEmpty(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinal) {
                                    Call<TranslateResponse> callAccept = WebserviceUtil.getInstance().translate(API_KEY,text,resourceLanguageCode,targetLanguageCode);
                                    callAccept.enqueue(new Callback<TranslateResponse>() {
                                        @Override
                                        public void onResponse(Call<TranslateResponse> call, Response<TranslateResponse> response) {
                                            Log.d("TAG", "onResponse: " + response.body().getTranslateData().getTranslations().toString().substring(1,response.body().getTranslateData().getTranslations().toString().length()-1));
                                            Translate translate=new Translate();
                                            translate.setLangcode_from(resourceLanguageCode);
                                            translate.setLangcode_to(targetLanguageCode);
                                            translate.setText_from(text);
                                            translate.setFlag_from(flagFrom);
                                            translate.setFlag_to(flagTo);
                                            translate.setText_to(response.body().getTranslateData().getTranslations().toString().substring(1,response.body().getTranslateData().getTranslations().toString().length()-1));
                                            translate.setLanguage_from(languageFrom);
                                            translate.setLanguage_to(languageTo);
                                            translateDBHelper.insertTranslate(translate);

                                            boolean autoBackground=sharedpreferences.getBoolean("autoBackground", false);
                                            Log.d("Delete",String.valueOf(voiceSpeed));
                                            if(autoBackground){
                                                mRecyclerView.setAdapter(new TranslateAdapter2(translateDBHelper.getAllTranslates(),getApplication()));
                                                adapter2.insert(0,translate);

                                            }
                                            else {
                                                mRecyclerView.setAdapter(new TranslateAdapter(translateDBHelper.getAllTranslates(),getApplication()));
                                                adapter2.insert(0,translate);
                                            }

                                            mRecyclerView.invalidate();
                                            mRecyclerView.smoothScrollToPosition(0);
                                            mRecyclerView.setBackground(null);

                                            fabListen.animate().scaleX(1).scaleY(1).start();
                                            stopVoiceRecorder();
                                            mSpeechService.removeListener(mSpeechServiceListener);
                                            unbindService(mServiceConnection);
                                            mSpeechService = null;
                                            Log.d("service","stop");

                                        }
                                        @Override
                                        public void onFailure(Call<TranslateResponse> call, Throwable t) {
                                            Toast.makeText(MainActivity.this, "Something was wrong. Try it again!",
                                                    Toast.LENGTH_LONG).show();
                                            fabListen.animate().scaleX(1).scaleY(1).start();
                                        }
                                    });

                                } else {
                                }
                            }
                        });
                    }
                }
            };

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.sourceLanguage:
                    final Dialog dialogSoureLaguage = new Dialog(MainActivity.this);
                    // khởi tạo dialog
                    dialogSoureLaguage.setContentView(R.layout.dialog_resource_language);
                    // xét layout cho dialog
                    dialogSoureLaguage.setTitle("Select Language");
                    ListView dialogSoureLaguage_ListView = (ListView)dialogSoureLaguage.findViewById(R.id.dialoglist);
                    ArrayList<Language> listLanguageSource=new ArrayList<Language>();
                    listLanguageSource=languageDBHelper.getLanguageList();
                    ListLanguageAdapter adapterResource =
                            new ListLanguageAdapter(MainActivity.this,
                                    android.R.layout.simple_list_item_1, listLanguageSource);
                    dialogSoureLaguage_ListView.setAdapter(adapterResource);
                    dialogSoureLaguage_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view,
                                                int position, long id) {
                            Language obj = (Language) (adapterView.getItemAtPosition(position));
                            String title = String.valueOf(obj.getLanguageName());
                            if(String.valueOf(obj.getLanguageCode()).equals(targetLanguageCode)){
                                targetLanguageCode=resourceLanguageCode;
                                if (sourceLanguage.getText().length() > 10) {
                                    String truncated = sourceLanguage.getText().subSequence(0, 10).toString().concat("...");
                                    targetLanguage.setText(truncated);
                                }
                                else {
                                    targetLanguage.setText(sourceLanguage.getText());
                                }

                                flagTo=flagFrom;
                                languageTo=languageFrom;
                                int resID = getApplication().getResources().getIdentifier(flagTo, "drawable", getApplicationContext().getPackageName());
                                flagIconTo.setImageResource(resID);

                            }
                            if (title.length() > 10) {
                                String truncated = title.subSequence(0, 12).toString().concat("...");
                                sourceLanguage.setText(truncated);
                            }
                            else {
                                sourceLanguage.setText(title);
                            }
                            flagFrom=obj.getIcon();
                            int resID = getApplication().getResources().getIdentifier(flagFrom, "drawable", getApplicationContext().getPackageName());
                            flagIconFrom.setImageResource(resID);
                            resourceLanguageCode=String.valueOf(obj.getLanguageCode());
                            languageFrom=obj.getLanguageName();
                            speechCodeSource=String.valueOf(obj.getSpeechCode());
                            Log.d("Long",resourceLanguageCode+"-----"+speechCodeSource);
                            dialogSoureLaguage.dismiss();
                        }});
                    dialogSoureLaguage.show();
                    break;
                case R.id.targetLanguage:
                    final Dialog dialogTargetLanguage = new Dialog(MainActivity.this);
                    dialogTargetLanguage.setContentView(R.layout.dialog_resource_language);
                    dialogTargetLanguage.setTitle("Select Language");
                    ListView dialogTargetLanguage_ListView = (ListView)dialogTargetLanguage.findViewById(R.id.dialoglist);
                    ArrayList<Language> listLanguageTarget=new ArrayList<Language>();
                    listLanguageTarget=languageDBHelper.getLanguageList();
                    ListLanguageAdapter adapterTarget =
                            new ListLanguageAdapter(MainActivity.this,
                                    android.R.layout.simple_list_item_1, listLanguageTarget);

                    dialogTargetLanguage_ListView.setAdapter(adapterTarget);

                    dialogTargetLanguage_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view,
                                                int position, long id) {
                            Language obj = (Language) (adapterView.getItemAtPosition(position));
                            String title = String.valueOf(obj.getLanguageName());
                            if(String.valueOf(obj.getLanguageCode()).equals(resourceLanguageCode)){
                                resourceLanguageCode=targetLanguageCode;
                                if (targetLanguage.getText().length() > 10) {
                                    String truncated = targetLanguage.getText().subSequence(0, 10).toString().concat("...");
                                    sourceLanguage.setText(truncated);
                                }
                                else {
                                    sourceLanguage.setText(targetLanguage.getText());
                                }
                                flagFrom=flagTo;
                                languageFrom=languageTo;
                                Log.d("Long",speechCodeTarget+"-----"+speechCodeTarget);
                                speechCodeSource=speechCodeTarget;
                                int resID = getApplication().getResources().getIdentifier(flagFrom, "drawable", getApplicationContext().getPackageName());
                                flagIconFrom.setImageResource(resID);

                            }
                            if (title.length() > 10) {
                                String truncated = title.subSequence(0, 12).toString().concat("...");
                                Log.d("Truncat",truncated);
                                targetLanguage.setText(truncated);
                            }
                            else {
                                targetLanguage.setText(title);
                            }
                            flagTo=obj.getIcon();
                            int resID = getApplication().getResources().getIdentifier(flagTo, "drawable", getApplicationContext().getPackageName());
                            flagIconTo.setImageResource(resID);
                            targetLanguageCode=String.valueOf(obj.getLanguageCode());
                            speechCodeTarget=String.valueOf(obj.getSpeechCode());
                            languageTo=obj.getLanguageName();
                            Log.d("Long",targetLanguageCode+"-----"+speechCodeSource);
                            dialogTargetLanguage.dismiss();
                        }});
                    dialogTargetLanguage.show();
                    break;
            }
        }
    };
}
