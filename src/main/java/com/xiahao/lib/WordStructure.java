package com.xiahao.lib;

public class WordStructure {
    public String word;
    public int entriesInDatabase;
    public int wordsInEntry;
    public double TF;
    public double IDF;
    public double TF_IDF;
    public boolean calculated;

    public WordStructure(String word){
        this.word = word;
        this.entriesInDatabase = 0;
        this.wordsInEntry = 0;
        this.TF = 0.0;
        this.IDF = 0.0;
        this.TF_IDF = 0.0;
        this.calculated = false;
    }
}
