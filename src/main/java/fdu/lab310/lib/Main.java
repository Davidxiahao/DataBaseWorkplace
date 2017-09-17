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

import java.util.*;

/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
        Set<String> targetStr = new HashSet<>();
        targetStr.add("himachat-term");
        targetStr.add("s3-website-ap-northeast-1");

        String helperStr = "com.altrthink.hima_chat";
        WordSegmentationUtil.segmentWord(targetStr, helperStr).forEach(System.out::println);
    }
}
