package fdu.lab310.lib;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ResultModel;
import com.xiahao.lib.FileOperator;
import com.xiahao.lib.FindMIX;
import com.xiahao.lib.Url;
import com.xiahao.lib.WordSegmentationUtil;

import java.io.File;
import java.util.*;


/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
        List<String> input = FileOperator.readFileByCharacter("public_suffix_list_before");
        Iterator<String> stringIterator = input.iterator();

        while (stringIterator.hasNext()){
            String line = stringIterator.next();
            if (line.length() == 0 || line.indexOf("//") == 0){
                stringIterator.remove();
            }
        }

        FileOperator.putLinesToFile("public_suffix_list", String.join("\n", input));
    }
}
