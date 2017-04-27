package com.google.cloud.android.speech.models;

/**
 * Created by longdo on 31/03/2017.
 */

public class Language {
    private int id;
    private String languageName;
    private String languageCode;
    String icon;

    public Language(int id, String languageName, String languageCode, String icon) {
        this.id = id;
        this.languageName = languageName;
        this.languageCode = languageCode;
        this.icon = icon;
    }

    public Language() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
