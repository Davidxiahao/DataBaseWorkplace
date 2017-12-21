package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.FileOperator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrepareGuessLanguage {
    public static void main(String[] args) {
        List<ggsearchModel> sourceData = OriginDbService.getInstance().getAllDataFromggsearch_copy();
        //List<ggsearchModel> sourceData = OriginDbService.getInstance().getAllDataFromggsearch_full();
        Set<String> snippetSet = new HashSet<>();

        for (ggsearchModel line : sourceData){
            line.mainwordsnippet = line.mainwordsnippet.replaceAll("\n", "");
            for (String string : line.mainwordsnippet.split("]@\\[")){
                if (string.indexOf("http")!=0){
                    snippetSet.add(string);
                }
            }
//            line.urlssnippet = line.urlssnippet.replaceAll("\n", "");
//            for (String string : line.urlssnippet.split("]@\\[")){
//                if (string.indexOf("http")!=0){
//                    snippetSet.add(string);
//                }
//            }
//            line.urlssnippetfull = line.urlssnippetfull.replaceAll("\n", "");
//            for (String string : line.urlssnippetfull.split("}@\\{")){
//                if (!string.equals("")){
//                    for (String str : string.split("]@\\[")){
//                        if (str.indexOf("http")!=0){
//                            snippetSet.add(str);
//                        }
//                    }
//                }
//            }
        }

        FileOperator.putLinesToFile("snippetdata3", String.join("\n", snippetSet));
    }
}
