package com.xiahao.lib;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.WordsIDFModel;
import fj.P;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class BuildWordList {

    public static List<OriginModel> getAllRecords(){
        OriginDbService originDbService = OriginDbService.getInstance();
        List<OriginModel> input = new ArrayList<>();
        input.addAll(originDbService.
                getAllDataFromTable("final_origin_domestic"));
        input.addAll(originDbService.
                getAllDataFromTable("final_origin_gp4w"));
        input.addAll(originDbService.
                getAllDataFromTable("final_origin_gp_top540"));
        input.addAll(originDbService.
                getAllDataFromTable("final_origin_yyb0818_13w"));
        return input;
    }

    public static List<String> fisrtWordList(List<OriginModel> input){
        List<String> result = new ArrayList<>();

        Map<String, Set<String>> map = new HashMap<>();
        for (OriginModel line : input){
            if (map.containsKey(line.apk)){
                Set<String> wordset = new HashSet<>();
                wordset.addAll(map.get(line.apk));
                //wordset.addAll(getWordsFromPN(Arrays.asList(line)));
                //wordset.addAll(getWordsFromDC(Arrays.asList(line)));
                wordset.addAll(getWordsFromUrl(Arrays.asList(line)));
                map.put(line.apk, wordset);
            }
            else {
                map.put(line.apk, new HashSet<>());
            }
        }

        for (Map.Entry<String, Set<String>> entry : map.entrySet()){
            result.addAll(entry.getValue());
        }

        return result;
    }

    private static List<String> getWordsFromUrl(List<OriginModel> list){
        List<String> result = new ArrayList<>();

        for (OriginModel line : list){
            for (String url : line.webOrigins.split(";")){
                if (isValidUrl(url)) {
                    try {
                        URI uri = new URI(url);
                        String string = uri.getHost();
                        result.addAll(Arrays.asList(string.split("\\.")));
                    } catch (URISyntaxException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        return result;
    }

    public static boolean isValidUrl(String urlString){
        URI uri = null;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }

        if(uri.getHost() == null){
            return false;
        }
        if(uri.getScheme() == null){
            return false;
        }
        if(uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")){
            return true;
        }
        return false;
    }

    private static List<String> getWordsFromDC(List<OriginModel> list){
        List<String> result = new ArrayList<>();

        for (OriginModel line : list){
            List<String> buffer = new ArrayList<>();
            buffer.addAll(Arrays.asList(line.declaringClass.
                    split("\\.")));
            int len = buffer.size();
            buffer.remove(len-1);
            result.addAll(buffer);
        }

        return result;
    }

    private static List<String> getWordsFromPN(List<OriginModel> list){
        List<String> result = new ArrayList<>();

        for (OriginModel line : list){
            result.addAll(Arrays.asList(line.codeOrigins.split("\\[PN]")[1].
                    split(";")[0].split("\\.")));
        }

        return result;
    }

    public static void main(String[] args) {
        List<OriginModel> input = new ArrayList<>();
        input.addAll(BuildWordList.getAllRecords());
        Map<String, Integer> summap = StringIDF.calculateStringFrequency(BuildWordList.fisrtWordList(input));
        Map<String, Double> map = StringIDF.calculateStringTFIDF(summap, input.size());

        map.remove("");
        for (String string : CreateWhiteList.whiteList){
            map.remove(string);
        }

        List<WordsIDFModel> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : map.entrySet()){
            result.add(new WordsIDFModel(entry.getKey(), entry.getValue(), summap.get(entry.getKey())));
        }

        Iterator<WordsIDFModel> iterator = result.iterator();
        while (iterator.hasNext()){
            WordsIDFModel word = iterator.next();
            if (word.sum < 10){
                iterator.remove();
            }
            else if (CreateWhiteList.commonWords.contains(word.word)){
                iterator.remove();
            }
            else if (word.word.contains(":") || isDigit(word.word)){
                iterator.remove();
            }
        }



        result.forEach(line -> System.out.println(line.word));

        //ResultDbService.getInstance().insertIntoWordsIDF(result);
        //ResultDbService.getInstance().insertIntoAfterRemove(result);
    }

    public static boolean isDigit(String strNum){
        return strNum.matches("[0-9]{1,}");
    }
}
