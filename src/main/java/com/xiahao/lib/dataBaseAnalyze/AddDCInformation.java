package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.DCInformationModel;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.DCInformation;
import com.xiahao.lib.machinelearning.countWords;
import com.xiahao.lib.machinelearning.makeVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDCInformation {
    public static void main(String[] args) {
        List<DCInformationModel> searchResult = OriginDbService.getInstance().getAllDCInformationData("DCInformation");
        Map<String, String> DC2SearchResult = new HashMap<>();

        for (DCInformationModel line : searchResult){
            if (line.mainwordsnippet!=null && !line.mainwordsnippet.equals("")){
                DC2SearchResult.put(line.DC, line.mainwordsnippet);
            }
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

        List<OriginModel> lastResult = AddPNandAN.getAllData();
        for (OriginModel line : lastResult){
            if (DC2SearchResult.containsKey(line.declaringClass)){
                String temp = "";
                double min = 2.0;
                for (String url : line.webOrigins.split(";")) {
                    if (urlMap.containsKey(url)) {
                        List<String> mainwordsList = new ArrayList<>(countWords.getWordsVector(DC2SearchResult.get(line.declaringClass)));
                        List<String> urlList = new ArrayList<>(countWords.getWordsVector(urlMap.get(url)));
                        double similarity = makeVector.getSimilarity(mainwordsList, urlList);
                        if (Double.isNaN(similarity)) similarity = -1.0;
                        if (min > similarity) {
                            min = similarity;
                            temp = line.declaringClass;
                        }
                    }
                }
                if (min!=2.0 && min >= 0.34 && line.similarity < 0.34){
                    System.out.println(line.idx+" "+min+" "+line.similarity+" "+temp+" "+line.webOrigins);
                }
            }
        }
    }
}
