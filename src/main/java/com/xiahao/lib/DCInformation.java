package com.xiahao.lib;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.DCInformationModel;
import com.hankz.util.dbutil.OriginModel;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixListFactory;

import java.util.*;

public class DCInformation {

    private static PublicSuffixListFactory factory = new PublicSuffixListFactory();
    private static PublicSuffixList suffixList = factory.build();

    public static void main(String[] args) {
        Map<String, DCInformationStructure> result = getAllData();

        List<DCInformationModel> resultList = new ArrayList<>();
        for (Map.Entry<String, DCInformationStructure> entry : result.entrySet()){
            DCInformationStructure value = entry.getValue();
            DCInformationModel line = new DCInformationModel(
                    value.DC,
                    String.join(";", value.mainwords),
                    value.total_frequence,
                    value.different_APK_frequence,
                    "",
                    String.join(";", value.URLs)
            );
            resultList.add(line);
        }

        //ResultDbService.getInstance().createTableDCInformation();
        //ResultDbService.getInstance().insertDCInformation(resultList);
        OriginDbService.getInstance().insertDCInformation(resultList);
    }

    private static Map<String, DCInformationStructure> getAllData(){
        List<OriginModel> dataBase = new ArrayList<>(OriginDbService.getInstance().getAllDataFromTable("last_origin_gp8w_meaningful"));

        Map<String, DCInformationStructure> result = new HashMap<>();
        for (OriginModel line : dataBase){
            List<String> words = new ArrayList<>(Arrays.asList(line.declaringClass.split("\\.")));

            String DC;
            List<String> subwords = new ArrayList<>();
            if (words.size() <= 1) {
                DC = words.get(0);
                subwords.addAll(words);
            }
            else {
                for (int i = 0; i < words.size()-1; i++){
                    subwords.add(words.get(i));
                }
                DC = String.join(".", subwords);
            }

            DCInformationStructure info;
            if (!result.containsKey(DC)){
                info = new DCInformationStructure(DC);

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

                info.mainwords.addAll(Arrays.asList(keyWords.split("\\.")));
            }
            else {
                info = result.get(DC);
            }

            Collections.addAll(info.URLs, line.webOrigins.split(";"));
            info.APKs.add(line.apk);
            info.total_frequence++;
            info.different_APK_frequence = info.APKs.size();

            result.put(DC, info);
        }

        return result;
    }
}
