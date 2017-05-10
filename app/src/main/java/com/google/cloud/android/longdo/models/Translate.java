package com.google.cloud.android.longdo.models;

/**
 * Created by longdo on 18/04/2017.
 */

public class Translate {
    private int id;
    private String lang_from;
    private String lang_to;
    private String text_from;
    private String text_to;
    private String flag_from;
    private String flag_to;
    private int idLanguage;

    public int getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(int idLanguage) {
        this.idLanguage = idLanguage;
    }

    public Translate() {

    }

    public Translate(int id, String lang_from, String lang_to, String text_from, String text_to, String flag_from, String flag_to, int idLanguage) {
        this.id = id;
        this.lang_from = lang_from;
        this.lang_to = lang_to;
        this.text_from = text_from;
        this.text_to = text_to;
        this.flag_from = flag_from;
        this.flag_to = flag_to;
        this.idLanguage = idLanguage;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getLang_from() {
        return lang_from;
    }

    public void setLang_from(String lang_from) {
        this.lang_from = lang_from;
    }

    public String getLang_to() {
        return lang_to;
    }

    public void setLang_to(String lang_to) {
        this.lang_to = lang_to;
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
}
