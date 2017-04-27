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

package com.google.cloud.android.speech;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.android.speech.adapters.ListLanguageAdapter;
import com.google.cloud.android.speech.adapters.TranslateAdapter;
import com.google.cloud.android.speech.helps.CustomRVItemTouchListener;
import com.google.cloud.android.speech.helps.RecyclerViewItemClickListener;
import com.google.cloud.android.speech.models.Language;
import com.google.cloud.android.speech.models.Translate;
import com.google.cloud.android.speech.sqlites.LanguageDBHelper;
import com.google.cloud.android.speech.sqlites.TranslateDBHelper;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements MessageDialogFragment.Listener {

    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";

//    private static final String STATE_RESULTS = "results";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    private SpeechService mSpeechService;
    private static final String API_KEY = "AIzaSyCIWb579dBtIL5BflFopri9L1OB1Md8Wsk";

    private VoiceRecorder mVoiceRecorder;
    private TranslateDBHelper translateDBHelper;
    private LanguageDBHelper languageDBHelper;
    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            showStatus(true);
            if (mSpeechService != null) {
                mSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
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
            showStatus(false);
            if (mSpeechService != null) {
                mSpeechService.finishRecognizing();
            }
        }

    };

    // Resource caches
    private int mColorHearing;
    private int mColorNotHearing;

    // View references
    private TextView mStatus;
    private TextView mText;

    private RecyclerView mRecyclerView;

    String resourceLanguageCode="en",targetLanguageCode="ja";
    TranslateAdapter adapter;
    ArrayList<Translate> array_list;

    //Text To Speech Variable
    TextToSpeech t1;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mSpeechService = SpeechService.from(binder);
            mSpeechService.addListener(mSpeechServiceListener);
            mStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mSpeechService = null;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Resources resources = getResources();
        final Resources.Theme theme = getTheme();
        mColorHearing = ResourcesCompat.getColor(resources, R.color.status_hearing, theme);
        mColorNotHearing = ResourcesCompat.getColor(resources, R.color.status_not_hearing, theme);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mStatus = (TextView) findViewById(R.id.status);
        mText = (TextView) findViewById(R.id.text);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

//        final ArrayList<String> results = savedInstanceState == null ? null :
//                savedInstanceState.getStringArrayList(STATE_RESULTS);
//        mAdapter = new ResultAdapter(results);
        translateDBHelper = new TranslateDBHelper(this);
        languageDBHelper=new LanguageDBHelper(this);
        array_list = translateDBHelper.getAllTranslates();
        adapter = new TranslateAdapter(array_list, getApplication());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(this,
                mRecyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
//                mydb.deleteTranslate(array_list.get(position).getId());
//                Toast.makeText(MainActivity.this, "Single Click on position        :"+position,
//                        Toast.LENGTH_SHORT).show();
//                mRecyclerView.setAdapter(new TranslateAdapter(mydb.getAllTranslates(),getApplication()));
//                adapter.notifyDataSetChanged();
//                mRecyclerView.invalidate();
//                mRecyclerView.smoothScrollToPosition(0);
//                RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
//                itemAnimator.setAddDuration(1000);
//                itemAnimator.setRemoveDuration(1000);
//                mRecyclerView.setItemAnimator(itemAnimator);
                    String needSpeak=translateDBHelper.getTextToFromId(array_list.get(position).getId());
                    t1.speak(needSpeak, TextToSpeech.QUEUE_FLUSH, null);

            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }
        }));


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        Log.d("LOG----------------","hello");

    }
    @Override

    public boolean onOptionsItemSelected(final MenuItem item){
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.resourelanguege) {
            final Dialog dialog = new Dialog(MainActivity.this);
            // khởi tạo dialog
            dialog.setContentView(R.layout.dialog_resource_language);
            // xét layout cho dialog
            dialog.setTitle("Select Language");
            ListView dialog_ListView = (ListView)dialog.findViewById(R.id.dialoglist);
            ArrayList<Language> listLanguage=new ArrayList<Language>();
            listLanguage=languageDBHelper.getLanguageList();


            ListLanguageAdapter adapterResource =
                    new ListLanguageAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1, listLanguage);

            dialog_ListView.setAdapter(adapterResource);
            // xét tiêu đề cho dialog

            dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view,
                                        int position, long id) {
                    Language obj = (Language) (adapterView.getItemAtPosition(position));
                    item.setTitle(String.valueOf(obj.getLanguageName()));
                    resourceLanguageCode=String.valueOf(obj.getLanguageCode());
                    Log.d("Long",resourceLanguageCode);
                    dialog.dismiss();
                }});
            // bắt sự kiện cho nút đăng kí
            dialog.show();
        }
        if (id == R.id.targelanguage) {
            final Dialog  dialog = new Dialog(MainActivity.this);
            // khởi tạo dialog
            dialog.setContentView(R.layout.dialog_resource_language);
            // xét layout cho dialog
            dialog.setTitle("Select Language");
            ListView dialog_ListView = (ListView)dialog.findViewById(R.id.dialoglist);
            ArrayList<Language> listLanguage=new ArrayList<Language>();
            listLanguage=languageDBHelper.getLanguageList();


            ListLanguageAdapter adapterResource =
                    new ListLanguageAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1, listLanguage);

            dialog_ListView.setAdapter(adapterResource);
            // xét tiêu đề cho dialog


            // bắt sự kiện cho nút đăng kí
            dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view,
                                        int position, long id) {
                    Language obj = (Language) (adapterView.getItemAtPosition(position));
                    item.setTitle(String.valueOf(obj.getLanguageName()));
                    targetLanguageCode=String.valueOf(obj.getLanguageCode());
                    Log.d("Long",targetLanguageCode);
                    dialog.dismiss();
                }});
            // bắt sự kiện cho nút đăng kí
            dialog.show();
        }
        if(id==R.id.action_deleteAllTranslates){
            translateDBHelper.deleteAllTranslates();
            mRecyclerView.setAdapter(new TranslateAdapter(translateDBHelper.getAllTranslates(),getApplication()));
            mRecyclerView.invalidate();
            mRecyclerView.smoothScrollToPosition(0);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            itemAnimator.setAddDuration(1000);
            itemAnimator.setRemoveDuration(1000);
            mRecyclerView.setItemAnimator(itemAnimator);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();

        // Prepare Cloud Speech API
        bindService(new Intent(this, SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);

        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }
//Long write here
    @Override
    protected void onResume() {
        super.onResume();
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

    private void showStatus(final boolean hearingVoice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setTextColor(hearingVoice ? mColorHearing : mColorNotHearing);
            }
        });
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
                    if (mText != null && !TextUtils.isEmpty(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinal) {
                                    mText.setText(null);
                                    final Handler textViewHandler = new Handler();

                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            TranslateOptions options = TranslateOptions.newBuilder()
                                                    .setApiKey(API_KEY)
                                                    .build();
                                            final com.google.cloud.translate.Translate translate = options.getService();
                                            final Translation translation =
                                                    translate.translate(text, com.google.cloud.translate.Translate.TranslateOption.sourceLanguage(resourceLanguageCode),
                                                            com.google.cloud.translate.Translate.TranslateOption.targetLanguage(targetLanguageCode));
                                            Log.d("L",translation.getTranslatedText());
                                            textViewHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Translate translate=new Translate();
                                                    translate.setLang_from(resourceLanguageCode);
                                                    translate.setLang_to(targetLanguageCode);
                                                    translate.setText_from(text);
                                                    translate.setText_to(translation.getTranslatedText());
                                                    translateDBHelper.insertTranslate(translate);
                                                    mRecyclerView.setAdapter(new TranslateAdapter(translateDBHelper.getAllTranslates(),getApplication()));
                                                    mRecyclerView.invalidate();
                                                    mRecyclerView.smoothScrollToPosition(0);
                                                    RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                                                    itemAnimator.setAddDuration(1000);
                                                    itemAnimator.setRemoveDuration(1000);
                                                    mRecyclerView.setItemAnimator(itemAnimator);

                                                }
                                            });
                                            return null;
                                        }
                                    }.execute();
                                } else {
                                    mText.setText(text);
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

}
