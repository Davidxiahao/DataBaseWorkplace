package com.hankz.util.dbutil;

public class GooglePlayModel {
    public final String category;
    public final String update_time;
    public final String name;
    public final String rate;
    public final String score;
    public final String pkg_name;
    public final String requires_android;
    public final String current_version;
    public final String developers;
    public final String numDownloads;

    public GooglePlayModel(String category, String update_time, String name, String rate, String score, String pkg_name,
                           String requires_android, String current_version, String developers, String numDownloads){
        this.category = category;
        this.update_time = update_time;
        this.name = name;
        this.rate = rate;
        this.score = score;
        this.pkg_name = pkg_name;
        this.requires_android = requires_android;
        this.current_version = current_version;
        this.developers = developers;
        this.numDownloads = numDownloads;
    }
}
