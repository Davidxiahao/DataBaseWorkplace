package com.xiahao.lib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DCInformationStructure {
    public String DC;
    public Set<String> mainwords;
    public int numberOfWords;
    public List<String> wordsSequence;
    public List<Double> wordsValueSequence;
    public List<Integer> wordsLenSequence;
    public int total_frequence;
    public int different_APK_frequence;
    public Set<String> APKs;
    public Set<String> URLs;
    public String apks;
    public String urls;

    public DCInformationStructure(String DC){
        this.DC = DC;
        this.mainwords = new HashSet<>();
        this.numberOfWords = 0;
        this.wordsSequence = new ArrayList<>();
        this.wordsValueSequence = new ArrayList<>();
        this.wordsLenSequence = new ArrayList<>();
        this.total_frequence = 0;
        this.different_APK_frequence = 0;
        this.APKs = new HashSet<>();
        this.URLs = new HashSet<>();
    }
}
