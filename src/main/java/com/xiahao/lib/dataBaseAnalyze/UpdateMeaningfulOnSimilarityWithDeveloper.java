package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ggsearchModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateMeaningfulOnSimilarityWithDeveloper {
    public static void main(String[] args) {
        List<ggsearchModel> developer2Url = OriginDbService.getInstance().getAllDataFromggsearch_full();
        Map<String, Double> developerAndUrlSimilarity = new HashMap<>();

        for (ggsearchModel line : developer2Url){
            developerAndUrlSimilarity.put(line.mainwords+line.urls, line.similarity);
        }

        List<OriginModel> sourceData = SampleDbService.getInstance().getAllDataFromlast_origin_gp8w_meaningful();
        for (OriginModel line : sourceData){
            if (line.similarity!=-1.0) {
                double min = 2.0;
                String keyWord=line.keyWord;
                for (String url : line.webOrigins.split(";")) {
                    String tempurl = url;
                    if (url.contains("URL:")) {
                        tempurl = url.replaceFirst("URL:", "");
                    }
                    double similarity = developerAndUrlSimilarity.getOrDefault(line.developers+tempurl, -1.0);
                    if (similarity!=-1.0 && min>similarity){
                        min=similarity;
                        keyWord=line.developers;
                    }
                }
                if (min!=2.0 && min>line.similarity){
                    line.similarity = min;
                    line.keyWord = keyWord;
                }
            }
        }

        SampleDbService.getInstance().updatelast_origin_gp8w_meaningful(sourceData);
    }
}
