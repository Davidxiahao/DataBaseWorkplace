package com.xiahao.lib;

import java.util.regex.Pattern;

public class DomainIPUtil {
    static Pattern numPattern = Pattern.compile("[0-9\\.]+");
    static Pattern ipPattern = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");


    public static boolean isAllNum(String urlStr) {
        return numPattern.matcher(urlStr).matches();
    }

    public static boolean isIPAddr(String urlStr) {
        return ipPattern.matcher(urlStr).matches();
    }

    public static String getDomainFromIP(String ipAddr) {
        // TODO: 2017/9/15 finish this func.
        return ipAddr;
    }


    public static String checkAndParseShortUrl(String urlStr) {
        // TODO: 2017/9/15 Finish this func
        if (urlStr.contains("goo.gl")) {
            return urlStr;
        }
        return urlStr;
    }

    /*public static void main(String[] args) {
        testData();
    }

    private static void testData() {
        List<String> testList = Arrays.asList(
            "85.132.44.29","127.0.0.1", "192.168", "7.0", ".3"
        );

        testList.forEach(t -> {
            System.out.println(t + "\t" + isAllNum(t));
            System.out.println(t + "\t" + isIPAddr(t));
        });
    }*/
}