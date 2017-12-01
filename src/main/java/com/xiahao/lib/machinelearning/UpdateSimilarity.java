package com.xiahao.lib.machinelearning;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.ggsearchModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateSimilarity {
    public static void main(String[] args) {
        List<ggsearchModel> similarityList = OriginDbService.getInstance().getAllDataFromggsearch_copy();
        List<ggsearchModel> urlList = OriginDbService.getInstance().getAllDataFromggsearch();

        Map<String, String> urlMap = new HashMap<>();
        for (ggsearchModel line : urlList){
            //System.out.println(line.urls);
            for (int i=0; i<line.urls.split(";").length; i++){
                //System.out.println(i);
                urlMap.put(line.urls.split(";")[i], line.urlssnippet.split("}@\\{", line.urls.split(";").length)[i]);
            }
        }

        List<ggsearchModel> resultList = new ArrayList<>();
        for (ggsearchModel line : similarityList){
            if (line.similarity<0.3){
                if (line.urls.contains("URL:")){
                    line.urls = line.urls.replaceFirst("URL:", "");
                    System.out.println(line.idx+" "+line.urls);
                }

                if (urlMap.containsKey(line.urls)){
                    List<String> mainwordsList = new ArrayList<>();
                    List<String> urlMapList = new ArrayList<>();

                    mainwordsList.addAll(countWords.getWordsVector(line.mainwordsnippet));
                    urlMapList.addAll(countWords.getWordsVector(urlMap.get(line.urls)));

                    double similarity = makeVector.getSimilarity(mainwordsList, urlMapList);
                    if (Double.isNaN(similarity)) similarity = -1.0;

                    if (similarity>line.similarity){
                        ggsearchModel temp = new ggsearchModel(line.idx, "", "", "", "",
                                "");
                        temp.similarity = similarity;
                        resultList.add(temp);
                        System.out.println(line.idx);
                    }
                }
            }
        }

        OriginDbService.getInstance().updateggsearch_copyOnSimilarity(resultList);
        System.out.println(resultList.size());
    }
}
