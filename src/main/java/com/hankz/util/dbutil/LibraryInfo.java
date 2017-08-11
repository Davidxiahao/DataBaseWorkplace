package com.hankz.util.dbutil;

public class LibraryInfo {
    public final String lib;
    public final String fingerprint;
    public final String liborigins;
    public final int manual;

    public LibraryInfo(String lib, String fingerprint, String liborigins, int manual){
        this.lib = lib;
        this.fingerprint = fingerprint;
        this.liborigins = liborigins;
        this.manual = manual;
    }

    public LibraryInfo(String lib, String liborigins){
        this(lib, "", liborigins, 0);
    }

    public String getLib() {
        return lib;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getLiborigins() {
        return liborigins;
    }

    public int getmanual(){
        return manual;
    }
}
