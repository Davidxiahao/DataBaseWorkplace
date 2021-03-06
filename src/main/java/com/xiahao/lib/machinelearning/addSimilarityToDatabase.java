package com.xiahao.lib.machinelearning;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.FileOperator;

import java.util.ArrayList;
import java.util.List;

public class addSimilarityToDatabase {
    public static void main(String[] args) {
        List<ggsearchModel> dataList = new ArrayList<>(OriginDbService.getInstance().getAllDataFromggsearch_copy());

        for (ggsearchModel line : dataList){

            List<String> mainwordsList = new ArrayList<>(countWords.getWordsVector(line.mainwordsnippet));
            List<String> urlList = new ArrayList<>(countWords.getWordsVector(line.urlssnippet));

            line.similarity = makeVector.getSimilarity(mainwordsList, urlList);

//            if (line.mainwords.equals("www") && line.urls.equals("www.youtube.com")){
//                FileOperator.putLinesToFile("mainWords", String.join("\n", mainwordsList));
//                FileOperator.putLinesToFile("URLs", String.join("\n", urlList));
//                System.out.println(line.mainwords);
//                System.out.println(line.urls);
//                line.similarity = makeVector.getSimilarity(mainwordsList, urlList);
//                System.out.println(line.similarity);
//            }

            if (Double.isNaN(line.similarity)) line.similarity = -1.0;
            if (line.mainwordsnippet.isEmpty() || line.urlssnippet.isEmpty()){
                line.similarity = -1.0;
            }
        }

        OriginDbService.getInstance().updateggsearch_copyOnSimilarity(dataList);
    }
}
