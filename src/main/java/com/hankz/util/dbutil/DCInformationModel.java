package com.hankz.util.dbutil;

public class DCInformationModel {
    public String DC;
    public String mainwords;
    public int total_frequence;
    public int different_APK_frequence;
    public String APKs;
    public String URLs;

    public DCInformationModel(String DC, String mainwords, int total_frequence, int different_APK_frequence,
                              String APKs, String URLs){
        this.DC = DC;
        this.mainwords = mainwords;
        this.total_frequence = total_frequence;
        this.different_APK_frequence = different_APK_frequence;
        this.APKs = APKs;
        this.URLs = URLs;
    }
}
