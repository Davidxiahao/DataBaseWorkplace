package com.xiahao.lib.machinelearning;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.CreateWhiteList;
import com.xiahao.lib.DCInformationStructure;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixListFactory;

import java.util.*;

public class DC2UrlSimilarity {

    private static PublicSuffixListFactory factory = new PublicSuffixListFactory();
    private static PublicSuffixList suffixList = factory.build();

    public static void main(String[] args) {
        List<ggsearchModel> data = OriginDbService.getInstance().getAllDataFromggsearch_copy();
        Map<String, Double> map = new HashMap<>();
        for (ggsearchModel line : data){
            map.put(line.mainwords+line.urls, line.similarity);
        }

        List<OriginModel> list = getAllData();

        for (OriginModel line : list){
            //line.similarity = map.getOrDefault(line.keyWord+line.webOrigins, -1.0);
            double min = 2.0;
            List<String> declaringClassList = new ArrayList<>();
            declaringClassList.addAll(Arrays.asList(line.declaringClass.split("\\.")));
            declaringClassList = declaringClassList.subList(0, declaringClassList.size() - 1);
            line.declaringClass = String.join(".", declaringClassList);
            for (String urlString : line.webOrigins.split(";")) {
                double max = -2.0;
                boolean haveSearchAll = true;
                for (String string : line.keywords) {
                    if (!map.containsKey(string + urlString)){
                        haveSearchAll = false;
                    }
                    if (max < map.getOrDefault(string + urlString, -1.0)) {
                        max = map.getOrDefault(string + urlString, -1.0);
                        line.keyWord = string;
                    }
                }
                if (!haveSearchAll) max = -1.0;

                if (min > max) min = max;
            }
            line.similarity = min;
        }

        //ResultDbService.getInstance().createTablefinal_origin_gp8w_meaningful_copy();
        //ResultDbService.getInstance().insertfinal_origin_gp8w_meaningful_copy(list);
        OriginDbService.getInstance().insertfinal_origin_gp8w_meaningful_copy(list);
    }

    public static List<OriginModel> getAllData(){
        List<OriginModel> dataBase = new ArrayList<>();
        dataBase.addAll(OriginDbService.getInstance().getAllDataFromTable("last_origin_gp8w_meaningful"));

        for (OriginModel line : dataBase){
            List<String> words = new ArrayList<>();
            words.addAll(Arrays.asList(line.declaringClass.split("\\.")));

            List<String> invertedSequenceOfWords = new ArrayList<>();

            //remove the top word e.g. facebookloginactivity
            //and get inverted sequence e.g. translate cn.edu.fudan into fudan.edu.cn
            for (int i=words.size()-2; i>=0; i--){
                invertedSequenceOfWords.add(words.get(i));
            }

            String invertedSequenceOfDeclaringClassAsUrl = String.join(".", invertedSequenceOfWords);
            //get public suffix e.g. fudan.edu.cn get edu.cn
            String publicSuffixOfDeclaringClass = suffixList.getPublicSuffix(invertedSequenceOfDeclaringClassAsUrl);
            if (!CreateWhiteList.public_suffix_list.contains(publicSuffixOfDeclaringClass)){
                publicSuffixOfDeclaringClass = null;
            }

            //delete public suffix
            int iFlag;
            String keyWords;
            if (publicSuffixOfDeclaringClass != null){
                iFlag = invertedSequenceOfDeclaringClassAsUrl.lastIndexOf(publicSuffixOfDeclaringClass);
                StringBuffer stringBuffer = new StringBuffer(invertedSequenceOfDeclaringClassAsUrl);
                if (iFlag != -1){
                    stringBuffer = stringBuffer.replace(iFlag, iFlag + publicSuffixOfDeclaringClass.length(), "");
                    keyWords = stringBuffer.toString();
                }
                else {
                    keyWords = invertedSequenceOfDeclaringClassAsUrl;
                }
            }
            else {
                keyWords = invertedSequenceOfDeclaringClassAsUrl;
            }

            //List<String> keyWordsList = new ArrayList<>();

            for (String string : keyWords.split("\\.")){
                //keyWordsList.add(string);
                line.keywords.add(string);
            }

            //line.keyWord = keyWordsList.get(keyWordsList.size()-1);
        }

        return dataBase;
    }
}
