package fdu.lab310.lib;

import com.hankz.util.dbService.LibsDbService;
import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ResultModel;
import com.hankz.util.dbutil.gpTop540;
import com.xiahao.lib.FileOperator;
import com.xiahao.lib.FindMIX;
import com.xiahao.lib.Url;
import com.xiahao.lib.WordSegmentationUtil;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.File;
import java.util.*;


/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
        LibsDbService libsDbService = LibsDbService.getInstance();
        libsDbService.loadAllDataFromyyb0818_13w();
        libsDbService.buildHashMap();
        List<String> input = FileOperator.readFileByCharacter("apps_gp_20170215_top540_apk_hash_list");

        List<gpTop540> result = new ArrayList<>();

        for (String line : input){
            if (libsDbService.apphashTocodeOrigins.containsKey(line)){
                String value = libsDbService.apphashTocodeOrigins.get(line);
                System.out.println(value);
                gpTop540 newline = new gpTop540(Integer.parseInt(value.split(",")[0]),
                                                line,
                                                value.split("\\[PN]")[1].split(";")[0],
                                                value.split("\\[AN]")[1].split(";")[0]);
                System.out.println(newline.idx);
                System.out.println(newline.hash);
                System.out.println(newline.appname);
                System.out.println(newline.pkgname);
                result.add(newline);
            }
        }

        OriginDbService originDbService = OriginDbService.getInstance();
        originDbService.insertgpTop540(result);
    }
}
