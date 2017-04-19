package com.google.cloud.android.speech;

/**
 * Created by longdo on 18/04/2017.
 */

public class TranslateModel {
    private Integer id;
    private String lang_from;
    private String lang_to;
    private String text_from;
    private String text_to;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TranslateModel(Integer id, String lang_from, String lang_to, String text_from, String text_to) {
        this.id = id;
        this.lang_from = lang_from;
        this.lang_to = lang_to;
        this.text_from = text_from;
        this.text_to = text_to;
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
