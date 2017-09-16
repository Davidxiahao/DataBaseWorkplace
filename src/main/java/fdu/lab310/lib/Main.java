package fdu.lab310.lib;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.ExtendOriginModel;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ResultModel;
import com.xiahao.lib.CheckXSOP;
import com.xiahao.lib.Url;
import com.xiahao.lib.WordSegmentationUtil;
import fdu.lab310.lib.analysis.ApkAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
//        OriginDbService originDbService = OriginDbService.getInstance();
//        originDbService.table = "origins_gp4w_2";
//        originDbService.loadAllData(true);
//
//        ResultDbService resultDbService = ResultDbService.getInstance();
//
//        resultDbService.creatTableTest();
//
//        int count = 0;
//        List<ResultModel> list = new ArrayList<>();
//
//        for (OriginModel originModel : originDbService.cacheList){
//            count++;
//            if (CheckXSOP.isXSOP(originModel.webOrigins, originModel.codeOrigins.split("\\[PN]")[1].
//                    split(";")[0])){
//                list.add(new ResultModel(originModel.apk, ""));
//            }
//            else {
//                String sameString = "";
//                for (String string : CheckXSOP.sameString){
//                    sameString = sameString + string + ";";
//                }
//
//                list.add(new ResultModel(originModel.apk, sameString));
//            }
//            System.out.println(count);
//        }
//
//        resultDbService.insertTest(list);

        List<ExtendOriginModel> list = OriginDbService.getInstance().getExtendData();

        for (ExtendOriginModel select : list) {
            for (String string : select.information.webOrigins.split(";")) {
                System.out.println("=========================================================");
                System.out.println(string);
                System.out.println(select.information.codeOrigins);
                Url weburl = new Url(string);
                weburl.getHost();
                if (weburl.isMeaningfulUrl()) {
                    List<String> targetString = new ArrayList<>();
                    targetString.addAll(weburl.getIdentitiesOfHost());
                    Set<String> result = WordSegmentationUtil.segmentWord(targetString, select.information.codeOrigins.
                            toLowerCase());
                    for (String str : result) {
                        System.out.println(str);
                    }
                }
            }
        }
    }
}
