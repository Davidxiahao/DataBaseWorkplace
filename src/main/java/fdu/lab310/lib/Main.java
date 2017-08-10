package fdu.lab310.lib;

import com.hankz.util.dbService.OriginCheckerDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.LibraryInfo;
import fdu.lab310.lib.analysis.ApkAnalysis;
import soot.SootClass;

import java.util.List;

/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Using soot to detect, fingerprint and identify origins of Android libs");
        new ApkAnalysis("C:\\Users\\xiahao\\Desktop\\APK\\aio.instasaver.apk").doAnalysis();

        OriginCheckerDbService originCheckerDbService = new OriginCheckerDbService();
        LibraryInfo libraryInfo = new LibraryInfo("com.baidu.map", "map_sdk_of_baidu", "a;b;c;d;e;f");
        if (originCheckerDbService.insertLibraryInfo(libraryInfo)){
            System.out.print("insert into libs success");
        }
        else {
            System.out.print("insert into libs failure");
        }

        String fingerprintOfBaidumap = originCheckerDbService.getLibraryFingerprint("com.baidu.map");
        System.out.print("fingerprint of baidu map sdk is " + fingerprintOfBaidumap);
    }
}
