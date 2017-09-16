package com.xiahao.lib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PredefinedList {
    public final static List<String> INTERNATIONAL = Arrays.asList("com", "edu", "gov", "int", "mil", "net", "org",
            "biz", "info", "pro", "name", "museum", "coop", "aero", "xxx", "idv");
    public final static List<String> NATIONAL = Arrays.asList("au", "mo", "br", "de", "ru", "fr", "kr", "ca", "ky",
            "us", "za", "eu", "jp", "tw", "hk", "sg", "in", "uk", "cn", "co", "no", "io", "me", "at", "gl", "hr", "pl",
            "vn", "tv", "gr", "to", "tr", "be", "th", "es", "se", "it", "bg", "eg", "is", "su", "ph", "nl", "al", "pe",
            "il", "ua", "ae", "fi", "pk", "dk", "hu", "ch", "do", "cc", "ro", "ly", "ir", "re", "ar", "am", "sk", "mx",
            "my", "id", "im", "mn", "rs", "nz", "pa", "az", "st");
    public final static List<String> SUFFIX = Arrays.asList("css", "swf", "cgi", "gif", "jsp", "js", "caf", "nsp", "m3u",
            "mov", "xml", "aspx", "jpeg", "html", "shtml", "3gp", "apk", "jpg", "zip", "htm", "wmv", "xlsx", "mpg",
            "png", "docx", "woff", "mp4", "mp3", "txt", "pdf", "ppt", "doc", "php", "m3u8", "jsf", "xls", "asp");
    public final static List<String> PREFIX = Arrays.asList("www", "bbs", "play", "sdk", "ads", "ws", "payment",
            "maps", "market", "docs", "web", "mobile", "accounts", "app", "ad", "api", "wap", "oauth", "android",
            "secure", "dl", "video", "store", "cloud", "feedback", "book", "user", "checkin", "dev", "news", "login",
            "app3", "profil", "img", "partner", "service", "box", "build", "search", "nhis", "nip", "auth", "blog",
            "connect", "mw", "www1", "test", "help", "cdn", "sites", "support", "mall", "member");

    public final static List<String> CLOUD_SERVICE = Arrays.asList("amazonaws");


    public static Set<String> ignoredWordSetInWebHost = new HashSet() {
        {
            addAll(PredefinedList.INTERNATIONAL);
            addAll(PredefinedList.NATIONAL);
            addAll(PredefinedList.SUFFIX);
            addAll(PredefinedList.PREFIX);
            addAll(PredefinedList.CLOUD_SERVICE);
        }
    };



}