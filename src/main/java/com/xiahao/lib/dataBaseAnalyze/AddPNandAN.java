package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.SampleDbService;
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
                wordlist.addAll(line.keywords);
            }
        }

        List<ggsearchModel> ggsearchList = OriginDbService.getInstance().getAllDataFromggsearch_copy();
        Set<String> haveSearched = new HashSet<>();
        for (ggsearchModel line : ggsearchList){
            haveSearched.add(line.mainwords);
        }

        List<ggsearchModel> resultList = new ArrayList<>();
        int sum = 0;
        for (String string : wordlist){
            if (string.length()>=3 && !haveSearched.contains(string)){
                sum++;
                ggsearchModel temp = new ggsearchModel(sum, string, "", "", "", "");
                resultList.add(temp);
            }
        }
        System.out.println(sum);
        //OriginDbService.getInstance().insertIntoggsearch_wait(resultList);
    }

    public static List<OriginModel> getAllData(){
        List<OriginModel> dataBase = new ArrayList<>(SampleDbService.getInstance().getAllDataFromlast_origin_gp8w_meaningful());

        for (OriginModel line : dataBase){
            List<String> words = new ArrayList<>(Arrays.asList(line.declaringClass.split("\\.")));


            line.declaringClass = cutTheLast(words);
            List<String> apkWords = new ArrayList<>(Arrays.asList(line.apk.split("\\.")));
            line.ApkName = cutTheLast(apkWords);
            if (line.codeOrigins.contains("[PN]")){
                String packageName = line.codeOrigins.split("\\[PN]")[1].split(";")[0];
                if (packageName.equals(line.declaringClass)){
                    line.PackageName = "";
                }
                else {
                    line.PackageName = packageName;
                    List<String> pnWords = new ArrayList<>(Arrays.asList(line.PackageName.split("\\.")));
                    //System.out.println("1 "+pnWords);
                    removePublicSuffix(line, pnWords);
                }
            }
            else {
                line.PackageName = "";
            }

            //System.out.println("2 "+line.PackageName);

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

        //System.out.println("3 "+keyWords);
        for (String string : keyWords.split("\\.")){
            //keyWordsList.add(string);
            if (!line.keywords.contains(string)) {
                line.keywords.add(string);
            }
        }
    }
}
