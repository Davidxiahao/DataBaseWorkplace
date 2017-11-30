package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.CreateWhiteList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixListFactory;

import java.util.*;

public class AddPNandAN {

    private static PublicSuffixListFactory factory = new PublicSuffixListFactory();
    private static PublicSuffixList suffixList = factory.build();

    public static void main(String[] args) {
        List<OriginModel> list = getAllData();
        Set<String> wordlist = new HashSet<>();
        for (OriginModel line : list){
            if (!line.keywords.isEmpty()){
                for (String string : line.keywords){
                    wordlist.add(string);
                }
            }
        }

        List<ggsearchModel> ggsearchList = OriginDbService.getInstance().getAllDataFromggsearch_copy();
        Set<String> haveSearched = new HashSet<>();
        for (ggsearchModel line : ggsearchList){
            haveSearched.add(line.mainwords);
        }

        int sum = 0;
        for (String string : wordlist){
            if (!haveSearched.contains(string)){
                System.out.println(string);
                sum++;
            }
        }
        System.out.println(sum);
    }

    public static List<OriginModel> getAllData(){
        List<OriginModel> dataBase = new ArrayList<>();
        dataBase.addAll(OriginDbService.getInstance().getAllDataFromTable("last_origin_gp8w_meaningful_copy3"));

        for (OriginModel line : dataBase){
            List<String> words = new ArrayList<>();
            words.addAll(Arrays.asList(line.declaringClass.split("\\.")));


            line.declaringClass = cutTheLast(words);
            List<String> apkWords = new ArrayList<>();
            apkWords.addAll(Arrays.asList(line.apk.split("\\.")));
            line.ApkName = cutTheLast(apkWords);
            if (line.codeOrigins.contains("[PN]")){
                String packageName = line.codeOrigins.split("\\[PN]")[1].split(";")[0];
                if (packageName.equals(line.declaringClass)){
                    line.PackageName = "";
                }
                else {
                    line.PackageName = packageName;
                    List<String> pnWords = new ArrayList<>();
                    pnWords.addAll(Arrays.asList(line.PackageName.split("\\.")));
                    removePublicSuffix(line, pnWords);
                }
            }
            else {
                line.PackageName = "";
            }

            if (!line.ApkName.equals(line.declaringClass)) removePublicSuffix(line, apkWords);


            //line.keyWord = keyWordsList.get(keyWordsList.size()-1);
        }

        return dataBase;
    }

    private static String cutTheLast(List<String> words){
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

        return DC;
    }

    private static void removePublicSuffix(OriginModel line, List<String> words){
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
            if (!line.keywords.contains(string)) {
                line.keywords.add(string);
            }
        }
    }
}
