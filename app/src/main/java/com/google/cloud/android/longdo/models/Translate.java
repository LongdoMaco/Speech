package com.google.cloud.android.longdo.models;

/**
 * Created by longdo on 18/04/2017.
 */

public class Translate {
    private int id;
    private String langcode_from;
    private String langcode_to;
    private String text_from;
    private String text_to;
    private String flag_from;
    private String flag_to;

    private String language_from;
    private String language_to;



    public Translate() {

    }

    public Translate(int id, String langcode_from, String langcode_to, String text_from, String text_to, String flag_from, String flag_to, String language_from, String language_to) {
        this.id = id;
        this.langcode_from = langcode_from;
        this.langcode_to = langcode_to;
        this.text_from = text_from;
        this.text_to = text_to;
        this.flag_from = flag_from;
        this.flag_to = flag_to;
        this.language_from = language_from;
        this.language_to = language_to;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLangcode_from() {
        return langcode_from;
    }

    public void setLangcode_from(String langcode_from) {
        this.langcode_from = langcode_from;
    }

    public String getLangcode_to() {
        return langcode_to;
    }

    public void setLangcode_to(String langcode_to) {
        this.langcode_to = langcode_to;
    }

    public String getText_from() {
        return text_from;
    }

    public void setText_from(String text_from) {
        this.text_from = text_from;
    }

    public String getText_to() {
        return text_to;
    }

    public void setText_to(String text_to) {
        this.text_to = text_to;
    }

    public String getFlag_from() {
        return flag_from;
    }

    public void setFlag_from(String flag_from) {
        this.flag_from = flag_from;
    }

    public String getFlag_to() {
        return flag_to;
    }

    public void setFlag_to(String flag_to) {
        this.flag_to = flag_to;
    }

    public String getLanguage_from() {
        return language_from;
    }

    public void setLanguage_from(String language_from) {
        this.language_from = language_from;
    }

    public String getLanguage_to() {
        return language_to;
    }

    public void setLanguage_to(String language_to) {
        this.language_to = language_to;
    }
}
