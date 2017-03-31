package com.google.cloud.android.speech;

/**
 * Created by longdo on 31/03/2017.
 */

public class Language {
    private String CountryName;
    private String CountryCode;
    int flag;

    public Language(String CountryName, String CountryCode, int flag) {
        this.CountryName = CountryName;
        this.CountryCode = CountryCode;
        this.flag = flag;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
