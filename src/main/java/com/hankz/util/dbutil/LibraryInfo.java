package com.hankz.util.dbutil;

public class LibraryInfo {
    public final String lib;
    public final String fingerprint;
    public final String constants;

    public LibraryInfo(String lib, String fingerprint, String constants){
        this.lib = lib;
        this.fingerprint = fingerprint;
        this.constants = constants;
    }
}
