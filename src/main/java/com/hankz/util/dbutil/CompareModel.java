package com.hankz.util.dbutil;

public class CompareModel {
    public int idx;
    public String apk;
    public String apkname;
    public String DC;
    public String AN;
    public String PN;

    public CompareModel(int idx, String apk, String apkname, String DC, String AN, String PN){
        this.idx = idx;
        this.apk = apk;
        this.apkname = apkname;
        this.DC = DC;
        this.AN = AN;
        this.PN = PN;
    }
}
