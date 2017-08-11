package fdu.lab310.lib;

import com.hankz.util.dbService.ApkHierarchy;
import com.hankz.util.dbService.LibsDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.LibraryInfo;
import com.hankz.util.dbutil.OriginInfo;
import fdu.lab310.lib.analysis.ApkAnalysis;
import soot.SootClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Using soot to detect, fingerprint and identify origins of Android libs");
        new ApkAnalysis("C:\\Users\\xiahao\\Desktop\\APK\\aio.instasaver.apk").doAnalysis();


    }
}
