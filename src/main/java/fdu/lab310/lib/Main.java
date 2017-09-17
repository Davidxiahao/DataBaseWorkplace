package fdu.lab310.lib;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ResultModel;
import com.xiahao.lib.Url;
import com.xiahao.lib.WordSegmentationUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
        OriginDbService originDbService = OriginDbService.getInstance();
        List<OriginModel> list = originDbService.getAllData();
        List<ResultModel> result = new ArrayList<>();
        int count = 0;

        for (OriginModel originModel : list){
            String sameString = "";
            Url url = new Url(originModel.webOrigins);
            if (url.isMeaningfulUrl()) {
                Set<String> set = new HashSet<>(url.getIdentitiesOfHost());
                for (String string : WordSegmentationUtil.segmentWord(set, originModel.codeOrigins.
                        split("\\[PN]")[1].split(";")[0])){
                    sameString = sameString + string;
                }
            }

            ResultModel resultModel = new ResultModel(originModel.apk, sameString);
            result.add(resultModel);
            count++;
            System.out.println(count);
            if (count % 10000 == 0){
                System.out.println("COMMIT");
                ResultDbService.getInstance().insertTest(result);
                result.clear();
            }
        }
    }
}
