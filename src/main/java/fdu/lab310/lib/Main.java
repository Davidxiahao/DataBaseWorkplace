package fdu.lab310.lib;

import com.hankz.util.dbService.OriginCheckerDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.LibraryInfo;
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

        OriginCheckerDbService originCheckerDbService = new OriginCheckerDbService();
        LibraryInfo libraryInfo = new LibraryInfo("com.baidu.map1", "map_sdk_of_baidu", "a;b;c;d");
        originCheckerDbService.insertLibraryInfo(libraryInfo);

        List<String> libs = originCheckerDbService.searchLibsByFingerprint("map_sdk_of_baidu");
        for(int i=0; i<libs.size(); i++){
            System.out.println(libs.get(i));
        }
    }
}
