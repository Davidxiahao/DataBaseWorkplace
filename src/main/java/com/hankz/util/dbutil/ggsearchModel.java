package com.hankz.util.dbutil;

public class ggsearchModel {
    public int idx;
    public String mainwords;
    public String urls;
    public String mainwordsnippet;
    public String urlssnippet;
    public String urlssnippetfull;

    public ggsearchModel(int idx, String mainwords, String urls, String mainwordsnippet, String urlssnippet,
                         String urlssnippetfull){
        this.idx = idx;
        this.mainwords = mainwords;
        this.urls = urls;
        this.mainwordsnippet = mainwordsnippet;
        this.urlssnippet = urlssnippet;
        this.urlssnippetfull = urlssnippetfull;
    }
}
