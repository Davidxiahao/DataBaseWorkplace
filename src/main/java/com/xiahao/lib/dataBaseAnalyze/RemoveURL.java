package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.ggsearchModel;

import java.util.List;

public class RemoveURL {
    public static void main(String[] args) {
        List<ggsearchModel> originalList = OriginDbService.getInstance().getAllDataFromggsearch_full();

        for (ggsearchModel line : originalList){
            if (line.urls.contains("URL:")){
                line.urls = line.urls.replaceAll("URL:", "");
            }
        }

        OriginDbService.getInstance().updateggsearchfullOnUrl(originalList);
    }
}
