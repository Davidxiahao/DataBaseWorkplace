package com.xiahao.lib;

import java.util.HashSet;
import java.util.Set;

public class URLInformationStructure {
    public String url;
    public Set<String> mainwords;
    public int total_frequence;
    public int different_DC_frequence;
    public int different_APK_frequence;
    public Set<String> DCs;
    public Set<String> APKs;

    public URLInformationStructure(String url){
        this.url = url;
        this.mainwords = new HashSet<>();
        this.total_frequence = 0;
        this.different_DC_frequence = 0;
        this.different_APK_frequence = 0;
        this.DCs = new HashSet<>();
        this.APKs = new HashSet<>();
    }
}
