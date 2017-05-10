package com.google.cloud.android.longdo.models;

/**
 * Created by longdo on 31/03/2017.
 */

public class Language {
    private int id;
    private String languageName;
    private String languageCode;
    String icon;
    String speechCode;
    String speechStatus;


    public Language() {
    }

    public Language(int id, String languageName, String languageCode, String icon, String speechCode, String speechStatus) {
        this.id = id;
        this.languageName = languageName;
        this.languageCode = languageCode;
        this.icon = icon;
        this.speechCode = speechCode;
        this.speechStatus = speechStatus;
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

    public String getSpeechCode() {
        return speechCode;
    }

    public void setSpeechCode(String speechCode) {
        this.speechCode = speechCode;
    }

    public String getSpeechStatus() {
        return speechStatus;
    }

    public void setSpeechStatus(String speechStatus) {
        this.speechStatus = speechStatus;
    }

}
