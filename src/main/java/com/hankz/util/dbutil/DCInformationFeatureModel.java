package com.hankz.util.dbutil;

public class DCInformationFeatureModel {
    public String DC;
    public String mainwords;
    public int numberOfWords;
    public String wordsSequence;
    public String wordsValueSequence;
    public String wordsLenSequence;
    public int total_frequence;
    public int different_APK_frequence;
    public String APKs;
    public String URLs;
    public int model_choice;

    public DCInformationFeatureModel(String DC, String mainwords, int numberOfWords, String wordsSequence,
                                     String wordsValueSequence, String wordsLenSequence, int total_frequence,
                                     int different_APK_frequence, String APKs, String URLs){
        this.DC = DC;
        this.mainwords = mainwords;
        this.numberOfWords = numberOfWords;
        this.wordsSequence = wordsSequence;
        this.wordsValueSequence = wordsValueSequence;
        this.wordsLenSequence = wordsLenSequence;
        this.total_frequence = total_frequence;
        this.different_APK_frequence = different_APK_frequence;
        this.APKs = APKs;
        this.URLs = URLs;
        this.model_choice = 0;
    }
}
