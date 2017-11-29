package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ggsearchModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateggsearchWait {
    public static void main(String[] args) {
        List<OriginModel> list = OriginDbService.getInstance().getAllDataFromTable("last_origin_gp8w_meaningful_copy");

        Set<String> urlSet = new HashSet<>();
        for (OriginModel line : list){
            if (line.similarity<0.4){
                if (line.webOrigins.contains("URL:")){
                    urlSet.add(line.webOrigins.replaceFirst("URL:", ""));
                }
                else {
                    urlSet.add(line.webOrigins);
                }
            }
        }

        List<ggsearchModel> result = new ArrayList<>();
        for (String string : urlSet){
            ggsearchModel temp = new ggsearchModel(0, "", string, "", "", "");
            result.add(temp);
        }

        OriginDbService.getInstance().insertIntoggsearch_wait(result);
    }
}
