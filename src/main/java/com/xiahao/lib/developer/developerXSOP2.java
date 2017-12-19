package com.xiahao.lib.developer;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.machinelearning.countWords;
import com.xiahao.lib.machinelearning.makeVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class developerXSOP2 {
    public static void main(String[] args) {
        List<ggsearchModel> urlData = OriginDbService.getInstance().getAllDataFromggsearch_full();
        List<ggsearchModel> developerData = OriginDbService.getInstance().getAllDataFromggsearch_copy();

        Map<String, String> developerMap = new HashMap<>();
        for (ggsearchModel line : developerData){
            developerMap.put(line.mainwords, line.mainwordsnippet);
        }

        Map<String, String> urlMap = new HashMap<>();
        for (ggsearchModel line : urlData){
            for (int i = 0; i < line.urls.split(";").length; i++){
                if (i < line.urlssnippetfull.split("}@\\{", -1).length){
                    urlMap.put(line.urls.split(";")[i],
                            line.urlssnippetfull.split("}@\\{", -1)[i]);
                }
            }
        }

        List<ggsearchModel> result = new ArrayList<>();
        List<OriginModel> relationshipData = SampleDbService.getInstance().getAllDataFromlast_origin_gp8w_meaningful();
        for (OriginModel line : relationshipData){
            //if (line.libNumHashOnly==0){
                for (String url : line.webOrigins.split(";")){
                    String tempUrl=url;
                    if (url.contains("URL:")){
                        tempUrl = url.replaceFirst("URL:", "");
                    }

                    if (urlMap.containsKey(tempUrl) && developerMap.containsKey(line.developers) && !developerMap.get(line.developers).equals("")){
                        List<String> mainwordList = new ArrayList<>(countWords.getWordsVector(developerMap.get(line.developers)));
                        List<String> urlList = new ArrayList<>(countWords.getWordsVector(urlMap.get(tempUrl)));

                        double similarity = makeVector.getSimilarity(mainwordList, urlList);
                        if (Double.isNaN(similarity)) similarity = -1.0;

                        ggsearchModel temp = new ggsearchModel(line.idx, line.developers, tempUrl, developerMap.get(line.developers), urlMap.get(tempUrl), "");
                        temp.similarity = similarity;
                        result.add(temp);
                    }
                }
            //}
        }

        OriginDbService.getInstance().insertIntoggsearch_full(result);
    }
}
