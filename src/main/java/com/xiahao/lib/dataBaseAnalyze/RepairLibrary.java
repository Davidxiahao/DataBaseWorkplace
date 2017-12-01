package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.LibraryHashModel;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.machinelearning.DC2UrlSimilarity;

import java.util.*;

public class RepairLibrary {
    public static void main(String[] args) {
        List<OriginModel> listGp8wCopy3 = DC2UrlSimilarity.getAllData();
        List<LibraryHashModel> listHashCopy3 = OriginDbService.getInstance().getAllDataFromLastLibraryGp8wHashCopy3();

        Map<Integer, String> libraryMap = new HashMap<>();
        for (LibraryHashModel line : listHashCopy3){
            libraryMap.put(line.idx, line.declareClass);
        }

        List<ggsearchModel> data = OriginDbService.getInstance().getAllDataFromggsearch_copy();
        Map<String, Double> map = new HashMap<>();
        for (ggsearchModel line : data){
            map.put(line.mainwords+line.urls, line.similarity);
        }

        for (OriginModel line : listGp8wCopy3){
            double min = 2.0;
            List<String> declaringClassList = new ArrayList<>(Arrays.asList(line.declaringClass.split("\\.")));
            declaringClassList = declaringClassList.subList(0, declaringClassList.size() - 1);
            line.declaringClass = String.join(".", declaringClassList);
            for (String urlString : line.webOrigins.split(";")) {
                double max = -1.0;
                for (String string : line.keywords) {
                    if (max < map.getOrDefault(string + urlString, -1.0)) {
                        max = map.getOrDefault(string + urlString, -1.0);
                        line.keyWord = string;
                    }
                }

                if (min > max) min = max;
            }
            line.similarity = min;
            if (line.libNum!=0){
                System.out.println(line.declaringClass+" "+libraryMap.get(line.libNum));
                min = 2.0;
                declaringClassList = new ArrayList<>(Arrays.asList(libraryMap.get(line.libNum).split("\\.")));
                declaringClassList = declaringClassList.subList(0, declaringClassList.size() - 1);
                for (String urlString : line.webOrigins.split(";")) {
                    double max = -1.0;
                    for (String string : declaringClassList) {
                        if (max < map.getOrDefault(string + urlString, -1.0)) {
                            max = map.getOrDefault(string + urlString, -1.0);
                            line.keyWord = string;
                        }
                    }

                    if (min > max) min = max;
                }
            }
            if (min > line.similarity) line.similarity = min;
        }

        OriginDbService.getInstance().insertfinal_origin_gp8w_meaningful_copy(listGp8wCopy3);
    }
}
