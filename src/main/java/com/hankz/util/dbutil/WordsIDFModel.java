package com.hankz.util.dbutil;

public class WordsIDFModel {
    public final String word;
    public final double IDF;
    public final int sum;

    public WordsIDFModel(String word, double IDF, int sum){
        this.word = word;
        this.IDF = IDF;
        this.sum = sum;
    }
}
