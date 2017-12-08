package com.xiahao.lib.machinelearning;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.DCInformationModel;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.CreateWhiteList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixListFactory;

import java.util.*;

public class DC2UrlSimilarity2 {
    private static PublicSuffixListFactory factory = new PublicSuffixListFactory();
    private static PublicSuffixList suffixList = factory.build();

    public static void main(String[] args) {
        List<ggsearchModel> data = OriginDbService.getInstance().getAllDataFromggsearch_copy();
        List<DCInformationModel> positionList = OriginDbService.getInstance().getAllDCInformationData("DCInformation");
        Map<String, DCInformationModel> string2Position = new HashMap<>();
        for (DCInformationModel line : positionList){
            string2Position.put(line.DC, line);
        }
        Map<String, Double> map = new HashMap<>();
        for (ggsearchModel line : data){
            map.put(line.mainwords+line.urls, line.similarity);
        }

        List<OriginModel> list = getAllData();

        for (OriginModel line : list){
            double min = 2.0;
            List<String> declaringClassList = new ArrayList<>(Arrays.asList(line.declaringClass.split("\\.")));
            declaringClassList = declaringClassList.subList(0, declaringClassList.size() - 1);
            line.declaringClass = String.join(".", declaringClassList);

            if (string2Position.containsKey(line.declaringClass)) {
                int position = string2Position.get(line.declaringClass).model_choice;
                position = line.keywords.size() - position;
                String keyword = line.keywords.get(position);
                for (String urlString : line.webOrigins.split(";")) {
                    double similarity = map.getOrDefault(keyword + urlString, -1.0);
                    if (similarity<min) {
                        min = similarity;
                        line.keyWord = keyword;
                    }
                }
                line.similarity = min;
            }
        }

        ResultDbService.getInstance().updateKeyword(list);
        //ResultDbService.getInstance().createTablefinal_origin_gp8w_meaningful_copy();
        //ResultDbService.getInstance().insertfinal_origin_gp8w_meaningful_copy(list);
    }

    public static List<OriginModel> getAllData(){
        List<OriginModel> dataBase = new ArrayList<>(OriginDbService.getInstance().getAllDataFromTable("last_origin_gp8w_meaningful"));

        for (OriginModel line : dataBase){
            List<String> words = new ArrayList<>(Arrays.asList(line.declaringClass.split("\\.")));

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

            Collections.addAll(line.keywords, keyWords.split("\\."));

            //line.keyWord = keyWordsList.get(keyWordsList.size()-1);
        }

        return dataBase;
    }
}
