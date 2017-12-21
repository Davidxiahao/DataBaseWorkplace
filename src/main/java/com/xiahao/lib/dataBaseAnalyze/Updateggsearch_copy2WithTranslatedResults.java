package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.FileOperator;

import java.util.*;

public class Updateggsearch_copy2WithTranslatedResults {
    public static void main(String[] args) {
        List<String> translatedResults = FileOperator.readFileByCharacter("translateresult3");
        Map<String, String> translator = new HashMap<>();

        for (int i=0; i<translatedResults.size(); i=i+2){
            if (!translatedResults.get(i).equals("error")) {
                translator.put(translatedResults.get(i), translatedResults.get(i + 1));
            }
        }

        List<ggsearchModel> sourceData = OriginDbService.getInstance().getAllDataFromggsearch_copy();
        //List<ggsearchModel> sourceData = OriginDbService.getInstance().getAllDataFromggsearch_full();

        for (ggsearchModel line : sourceData){
            line.mainwordsnippet = line.mainwordsnippet.replaceAll("\n", "");
            String temp = line.mainwordsnippet;
            for (String string : line.mainwordsnippet.split("]@\\[")){
                if (string.indexOf("http")!=0 && translator.containsKey(string)){
                    temp = temp.replace(string, translator.get(string));
                    System.out.println(string);
                    System.out.println(line.idx+" "+line.mainwordsnippet);
                    System.out.println(temp);
                }
            }
            line.mainwordsnippet = temp;
//
//            line.urlssnippet = line.urlssnippet.replaceAll("\n", "");
//            temp = line.urlssnippet;
//            for (String string : line.urlssnippet.split("]@\\[")){
//                if (string.indexOf("http")!=0 && translator.containsKey(string)){
//                    temp = temp.replace(string, translator.get(string));
//                    System.out.println(string);
//                    System.out.println(line.idx+" "+line.urlssnippet);
//                    System.out.println(temp);
//                }
//            }
//            line.urlssnippet = temp;
//            line.urlssnippetfull = line.urlssnippetfull.replaceAll("\n", "");
//            String temp = line.urlssnippetfull;
//            for (String string : line.urlssnippetfull.split("}@\\{")){
//                if (!string.equals("")){
//                    for (String str : string.split("]@\\[")){
//                        if (str.indexOf("http")!=0 && translator.containsKey(str)){
//                            temp = temp.replace(str, translator.get(str));
//                            System.out.println(string);
//                            System.out.println(line.idx+" "+line.urlssnippetfull);
//                            System.out.println(temp);
//                        }
//                    }
//                }
//            }
//            line.urlssnippetfull = temp;
        }

        //OriginDbService.getInstance().updateggsearch_copy2OnSnippet(sourceData);
        //OriginDbService.getInstance().updateggsearchfullOnUrlsnippetfull(sourceData);
        OriginDbService.getInstance().updateggsearch_copyOnSnippet(sourceData);
    }
}
