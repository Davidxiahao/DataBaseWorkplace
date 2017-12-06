package com.xiahao.lib.machinelearning;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.ggsearchModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ggsearchDistribution {
    public static void main(String[] args) {
        List<ggsearchModel> dataList = new ArrayList<>(OriginDbService.getInstance().getAllDataFromggsearch());

        List<ggsearchModel> result = new ArrayList<>();
        int idx = 0;
        for (ggsearchModel line : dataList){
            Map<String, String> mainwords = new HashMap<>();
            Map<String, String> urls = new HashMap<>();
            Map<String, String> urlsfull = new HashMap<>();

            for (int i = 0; i < line.mainwords.split(";").length; i++){
                if (i < line.mainwordsnippet.split("}@\\{").length) {
                    mainwords.put(line.mainwords.split(";")[i], line.mainwordsnippet.split("}@\\{")[i]);
                }
            }

            for (int i = 0; i < line.urls.split(";").length; i++){
                if (i < line.urlssnippet.split("}@\\{").length) {
                    urls.put(line.urls.split(";")[i], line.urlssnippet.split("}@\\{")[i]);
                }
                if (i < line.urlssnippetfull.split("}@\\{").length){
                    urlsfull.put(line.urls.split(";")[i], line.urlssnippetfull.split("}@\\{")[i]);
                }
            }

            for (Map.Entry<String, String> entry1 : mainwords.entrySet()){
                for (Map.Entry<String, String> entry2 : urls.entrySet()){
                    idx++;
                    ggsearchModel temp = new ggsearchModel(idx, entry1.getKey(), entry2.getKey(), entry1.getValue(),
                            entry2.getValue(), urlsfull.get(entry2.getKey()));
                    temp.allmainwords = line.mainwords;
                    temp.allurls = line.urls;
                    result.add(temp);
                }
            }
        }

        OriginDbService.getInstance().insertIntoggsearch_copy(result);
    }
}
