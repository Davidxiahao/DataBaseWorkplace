package com.xiahao.lib.dataBaseAnalyze;

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

public class Updateggsearchcopy2forPNandAN {
    public static void main(String[] args) {
        List<ggsearchModel> searchResults = OriginDbService.getInstance().getAllDataFromggsearch();
        Map<String, String> wordsMap = new HashMap<>();

        for (ggsearchModel line : searchResults){
            wordsMap.put(line.mainwords, line.mainwordsnippet);
        }

        List<ggsearchModel> haveCalculated = OriginDbService.getInstance().getAllDataFromggsearch_copy();
        Map<String, ggsearchModel> calculatedMap = new HashMap<>();

        for (ggsearchModel line : haveCalculated){
            calculatedMap.put(line.mainwords+line.urls, line);
        }

        List<ggsearchModel> haveSearched = OriginDbService.getInstance().getAllDataFromggsearch_full();
        Map<String, String> urlMap = new HashMap<>();
        for (ggsearchModel line : haveSearched){
            if (!line.mainwords.equals("")) {
                for (int i = 0; i < line.urls.split(";").length; i++) {
                    if (i < line.urlssnippet.split("}@\\{", -1).length) {
                        urlMap.put(line.urls.split(";")[i], line.urlssnippet.split("}@\\{", -1)[i]);
                    }
                }
            }
        }


        List<OriginModel> inputList = AddPNandAN.getAllData();
        for (OriginModel line : inputList) {
            if (line.similarity >= 0) {
                String temp = "";
                double min = 2.0;
                for (String url : line.webOrigins.split(";")) {
                    double max = -1.0;
                    String temperated = "";
                    for (String keyword : line.keywords) {
                        if (!calculatedMap.containsKey(keyword + url) && wordsMap.containsKey(keyword) && urlMap.containsKey(url)) {
                            List<String> mainwordsList = new ArrayList<>(countWords.getWordsVector(wordsMap.get(keyword)));
                            List<String> urlList = new ArrayList<>(countWords.getWordsVector(urlMap.get(url)));
                            double similarity = makeVector.getSimilarity(mainwordsList, urlList);
                            if (Double.isNaN(similarity)) similarity = -1.0;
                            if (similarity > max) {
                                max = similarity;
                                temperated = keyword;
                            }
                        }
                    }
                    if (max < min) {
                        min = max;
                        temp = temperated;
                    }
                }
                if (min > line.similarity) {
                    line.similarity = min;
                    line.keyWord = temp;
                    System.out.println(line.idx+" "+line.similarity+" "+line.keyWord+" "+line.webOrigins);
                }
            }
        }

        SampleDbService.getInstance().updatelast_origin_gp8w_meaningful(inputList);
    }
}
