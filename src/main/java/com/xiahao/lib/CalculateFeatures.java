package com.xiahao.lib;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.DCInformationFeatureModel;
import com.hankz.util.dbutil.DCInformationModel;

import java.util.*;

public class CalculateFeatures {
    public static void main(String[] args) {
        List<DCInformationModel> dataBase = new ArrayList<>(OriginDbService.getInstance().getAllDCInformationData("DCInformation"));

        List<DCInformationStructure> originalData = new ArrayList<>();
        for (DCInformationModel line : dataBase){
            DCInformationStructure temp = new DCInformationStructure(line.DC);
            temp.total_frequence = line.total_frequence;
            temp.different_APK_frequence = line.different_APK_frequence;

            temp.mainwords.addAll(Arrays.asList(line.mainwords.split(";")));

            for (String string : line.DC.split("\\.")){
                if (temp.mainwords.contains(string)){
                    temp.wordsSequence.add(string);
                }
            }

            temp.numberOfWords = feature_one_numberOfWords(line.mainwords);

            temp.wordsLenSequence.addAll(feature_three_lenOfWords(temp.wordsSequence));

            temp.apks = line.APKs;
            temp.urls = line.URLs;

            originalData.add(temp);

            //APKs and URLs will not be used temporally
        }

        List<DCInformationStructure> result = new ArrayList<>(feature_two_valueOfWords(originalData));

        List<numpydataStructure> numpydataList = new ArrayList<>();
        List<String> writeIntoFiles = new ArrayList<>();

//        List<String> modelResult = new ArrayList<>();
//        modelResult.addAll(FileOperator.readFileByCharacter("test.data"));
//        List<Double> modelResultList = new ArrayList<>();
//        for (String line : modelResult){
//            for (String string : line.split(" ")){
//                if (!string.equals("")) {
//                    modelResultList.add(Double.parseDouble(string));
//                }
//            }
//        }

        List<DCInformationFeatureModel> resultList = new ArrayList<>();
        //Iterator<Double> iterator = modelResultList.iterator();
        for (DCInformationStructure line : result){
            List<String> wordsValueSequence = new ArrayList<>();
            line.wordsValueSequence.forEach(w -> wordsValueSequence.add(Double.toString(w)));
            List<String> wordsLenSequence = new ArrayList<>();
            line.wordsLenSequence.forEach(w -> wordsLenSequence.add(Integer.toString(w)));
            DCInformationFeatureModel temp = new DCInformationFeatureModel(
                    line.DC,
                    String.join(";", line.mainwords),
                    line.numberOfWords,
                    String.join("->", line.wordsSequence),
                    "(" + String.join(",", wordsValueSequence) + ")",
                    "(" + String.join(",", wordsLenSequence) + ")",
                    line.total_frequence,
                    line.different_APK_frequence,
                    line.apks,
                    line.urls
            );

            //resultList.add(temp);

//            if (iterator.hasNext()){
//                    temp.model_choice = iterator.next().intValue();
//            }
            if (line.wordsLenSequence.size() > 3){
                line.wordsLenSequence = line.wordsLenSequence.subList(0, 3);
                line.wordsValueSequence = line.wordsValueSequence.subList(0, 3);
            }
            if (line.wordsLenSequence.size() <= 3){
                numpydataStructure numpydata = new numpydataStructure();
                numpydata.list.addAll(line.wordsValueSequence);
                for (int i = 0; i < 3-line.wordsValueSequence.size(); i++){
                    numpydata.list.add(0.0);
                }
                line.wordsLenSequence.forEach(w -> numpydata.list.add(new Double(w)));
                for (int i = 0; i < 3-line.wordsLenSequence.size(); i++){
                    numpydata.list.add(0.0);
                }

                String string = "";
                for (Double value : numpydata.list){
                    string = string + " " + value.toString();
                }
                string = string.substring(1,string.length());
                //string = line.DC + " "  + string;
                numpydataList.add(numpydata);
                writeIntoFiles.add(string);
            }

            resultList.add(temp);
        }

        FileOperator.putLinesToFile("test_data.txt", String.join("\n", writeIntoFiles));

//        ResultDbService.getInstance().updateDCInformationFeatureOnChoice(resultList);
//        ResultDbService.getInstance().createTableDCInformationFeature();
//        ResultDbService.getInstance().insertDCInformationFeature(resultList);
    }

    private static int feature_one_numberOfWords(String mainwords){
        int result = 0;
        for (String string : mainwords.split(";")){
            if (!string.equals("")){
                result++;
            }
        }

        return result;
    }

    private static List<DCInformationStructure> feature_two_valueOfWords(List<DCInformationStructure> list){

        Map<String, WordStructure> words = new HashMap<>();
        for (DCInformationStructure line : list){
            for (String string : line.mainwords){
                if (!string.equals("")){
                    WordStructure info;
                    if (!words.containsKey(string)){
                        info = new WordStructure(string);
                    }
                    else {
                        info = words.get(string);
                    }

                    info.entriesInDatabase++;
                    words.put(string, info);
                }
            }
        }

        int numberOfEntry = list.size();
        for (DCInformationStructure line : list){
            for (String string : line.wordsSequence){
                WordStructure info;

                info = words.get(string);
                int wordsInEntry = 0;

                for (String string1 : line.wordsSequence){
                    if (string.equals(string1)){
                        wordsInEntry++;
                    }
                }

                double TF = calculateTF(wordsInEntry, line.wordsSequence.size());
                double IDF = calculateIDF(numberOfEntry, info.entriesInDatabase);
                double TF_IDF = TF * IDF;

                words.put(string, info);

                line.wordsValueSequence.add(TF_IDF);
            }
        }

        return list;
    }

    public static double calculateTF(int x, int y){
        return (double)x/(double)y;
    }

    public static double calculateIDF(int x, int y){
        return Math.log10((double)x/(double)y);
    }

    private static List<Integer> feature_three_lenOfWords(List<String> wordsSequence){
        List<Integer> result = new ArrayList<>();

        for (String string : wordsSequence){
            result.add(string.length());
        }

        return result;
    }
}
