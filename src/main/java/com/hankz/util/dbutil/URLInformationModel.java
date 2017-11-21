package com.hankz.util.dbutil;

public class URLInformationModel {
    public String url;
    public String mainwords;
    public int total_frequence;
    public int different_DC_frequence;
    public int different_APK_frequence;
    public String DCs;
    public String APKs;

    public URLInformationModel(String url, String mainwords, int total_frequence, int different_DC_frequence,
                               int different_APK_frequence, String DCs, String APKs){
        this.url = url;
        this.mainwords = mainwords;
        this.total_frequence = total_frequence;
        this.different_DC_frequence = different_DC_frequence;
        this.different_APK_frequence = different_APK_frequence;
        this.DCs = DCs;
        this.APKs = APKs;
    }
}
