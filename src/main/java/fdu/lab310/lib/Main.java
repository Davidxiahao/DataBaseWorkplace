package fdu.lab310.lib;

import fdu.lab310.lib.analysis.ApkAnalysis;

/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Using soot to detect, fingerprint and identify origins of Android libs");
        new ApkAnalysis("C:\\Users\\xiahao\\Desktop\\APK\\aio.instasaver.apk").doAnalysis();


    }
}
