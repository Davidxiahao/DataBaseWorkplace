package com.hankz.util.dbutil;

public class LibraryHashModel {
    public int idx;
    public String api;
    public String webOrigins;
    public String declareClass;
    public int count;

    public LibraryHashModel(int idx, String api, String webOrigins, String declareClass, int count){
        this.idx = idx;
        this.api = api;
        this.webOrigins = webOrigins;
        this.declareClass = declareClass;
        this.count = count;
    }
}
