package com.google.cloud.android.longdo.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.cloud.android.longdo.R;
import com.google.cloud.android.longdo.helps.ImageConverter;
import com.google.cloud.android.longdo.models.Translate;
import com.google.cloud.android.longdo.sqlites.TranslateDBHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.google.cloud.android.longdo.R.id.flagFrom;
import static com.google.cloud.android.longdo.R.id.flagTo;


/**
 * Created by longdo on 18/04/2017.
 */

public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.View_Holder> {

    private List<Translate> translateList = Collections.emptyList();
    Context context;
    private TranslateDBHelper translateDBHelper;
    TextToSpeech textToSpeech;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;

    public TranslateAdapter(List<Translate> translateList, Context context) {
        this.translateList = translateList;
        this.context = context;
        translateDBHelper = new TranslateDBHelper(context.getApplicationContext());
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        holder.title.setText(translateList.get(position).getText_from());
        holder.description.setText(translateList.get(position).getText_to());

        if(translateList.get(position).getFlag_from()!=null && !"".equals(translateList.get(position).getFlag_from()))
            {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),context.getResources().getIdentifier(translateList.get(position).getFlag_from(), "drawable", context.getPackageName()));
                Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 80);
                holder.flag_from.setImageBitmap(circularBitmap);
            }
        else {
            holder.flag_from.setImageResource(R.drawable.vn);
        }
        if(translateList.get(position).getFlag_to()!=null && !"".equals(translateList.get(position).getFlag_to()))
        {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),context.getResources().getIdentifier(translateList.get(position).getFlag_to(), "drawable", context.getPackageName()));
            Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 80);
            holder.flag_to.setImageBitmap(circularBitmap);
        }
        else {
            holder.flag_to.setImageResource(R.drawable.vn);
        }

        if(position%2==0){
            holder.relativeLayout.setBackgroundResource(R.drawable.background_white);
        }else{
            holder.relativeLayout.setBackgroundResource(R.drawable.background_blue);

        }
        holder.language_from.setText(translateList.get(position).getLanguage_from());
        holder.language_to.setText(translateList.get(position).getLanguage_to());
        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Translate translate = translateList.get(position);
                translateDBHelper.deleteTranslate(translate.getId());
                translateList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,translateList.size());
            }
        });
        final String speechCode=translateDBHelper.getSpeechCodeFromId(translateList.get(position).getId());
        final float speedVoice=sharedpreferences.getFloat("SpeedVoice", 1f);
        final float pitchVoice=sharedpreferences.getFloat("VoicePitch", 1f);
        if("".equals(speechCode) || speechCode==null )
        {
            holder.mSpeakButton.setImageResource(R.drawable.mute24);
        }
        else holder.mSpeakButton.setImageResource(R.drawable.volume24);
        holder.mSpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toSpeak = translateList.get(position).getText_to();
                if ("FRENCH".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.FRENCH);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if ("UK".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.UK);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                } else if("US".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.US);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if("GERMAN".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.GERMAN);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if("CANADA_FRENCH".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.CANADA_FRENCH);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if ("ITALIAN".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.ITALIAN);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if("ENGLISH".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.ENGLISH);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if ("JAPANESE".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.JAPANESE);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if("KOREA".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.KOREA);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if("TRADITIONAL_CHINESE".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.TRADITIONAL_CHINESE);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if("CHINESE".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.CHINESE);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
                else if ("SIMPLIFIED_CHINESE".equals(speechCode)){
                    textToSpeech=new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                                textToSpeech.setSpeechRate(speedVoice);
                                textToSpeech.setPitch(pitchVoice);
                                playNextChunk(toSpeak);
                            }
                        }
                    });
                }
            }
        });

    }
    private void playNextChunk(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(text);
        } else {
            ttsUnder20(text);
        }}
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId);
    }

    @Override
    public int getItemCount() {
        if (translateList!=null){
            return translateList.size();
        }
        else return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Translate data) {
        translateList.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Translate data) {
        int position = translateList.indexOf(data);
        translateList.remove(position);
        if(translateList.size()==0)
        notifyItemRemoved(position);
    }
    /**
     * View holder class
     * */
    public class View_Holder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView language_from,language_to;
        TextView description;
        ImageView flag_from,flag_to;
        LinearLayout relativeLayout;
        ImageButton mRemoveButton;
        RecyclerView mRecylecleviewer;
        ImageButton mSpeakButton;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            language_from = (TextView) itemView.findViewById(R.id.languagefrom);
            language_to = (TextView) itemView.findViewById(R.id.languageto);
            description = (TextView) itemView.findViewById(R.id.description);
            flag_from = (ImageView) itemView.findViewById(flagFrom);
            flag_to = (ImageView) itemView.findViewById(flagTo);
            relativeLayout=(LinearLayout) itemView.findViewById(R.id.background);
            mRemoveButton=(ImageButton) itemView.findViewById(R.id.ib_remove);
            mRecylecleviewer=(RecyclerView) itemView.findViewById(R.id.recycler_view);
            mSpeakButton=(ImageButton) itemView.findViewById(R.id.speakBtn);
        }
    }
}
