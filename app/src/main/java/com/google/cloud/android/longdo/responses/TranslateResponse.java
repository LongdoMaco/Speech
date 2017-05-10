package com.google.cloud.android.longdo.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by longdo on 07/05/2017.
 */
public class TranslateResponse {
    @SerializedName("data")
    private TranslateData translateData;

    public TranslateData getTranslateData() {
        return translateData;
    }

    /**
     * TranslateData class
     */
    public static class TranslateData{
        @SerializedName("translations")
        private List<TranslationText> translations;

        public List<TranslationText> getTranslations() {
            return translations;
        }
    }

    /**
     * TranslationText class
     */
    public static class TranslationText {
        @SerializedName("translatedText")
        private String translatedText;

        public String getTranslatedText() {
            return translatedText;
        }

        @Override
        public String toString() {
            return translatedText;
        }
    }
}
