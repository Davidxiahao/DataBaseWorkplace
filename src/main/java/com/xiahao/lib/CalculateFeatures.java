package com.xiahao.lib;

import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.DCInformationFeatureModel;
import com.hankz.util.dbutil.DCInformationModel;

import java.util.*;

public class CalculateFeatures {
    public static void main(String[] args) {
        List<DCInformationModel> dataBase = new ArrayList<>();
        dataBase.addAll(ResultDbService.getInstance().getAllDCInformationData("DCInformation"));

        List<DCInformationStructure> originalData = new ArrayList<>();
        for (DCInformationModel line : dataBase){
            DCInformationStructure temp = new DCInformationStructure(line.DC);
            temp.total_frequence = line.total_frequence;
            temp.different_APK_frequence = line.different_APK_frequence;

            for (String string : line.mainwords.split(";")){
                temp.mainwords.add(string);
            }

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

        List<DCInformationStructure> result = new ArrayList<>();
        result.addAll(feature_two_valueOfWords(originalData));

        List<numpydataStructure> numpydataList = new ArrayList<>();
        List<String> writeIntoFiles = new ArrayList<>();

        List<String> modelResult = new ArrayList<>();
        modelResult.addAll(FileOperator.readFileByCharacter("test.data"));
        List<Double> modelResultList = new ArrayList<>();
        for (String line : modelResult){
            for (String string : line.split(" ")){
                if (!string.equals("")) {
                    modelResultList.add(Double.parseDouble(string));
                }
            }
        }

        List<DCInformationFeatureModel> resultList = new ArrayList<>();
        Iterator<Double> iterator = modelResultList.iterator();
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

            if (line.wordsLenSequence.size() <= 4){
                if (iterator.hasNext()){
                    temp.model_choice = iterator.next().intValue();
                }
                numpydataStructure numpydata = new numpydataStructure();
                numpydata.list.addAll(line.wordsValueSequence);
                for (int i = 0; i < 4-line.wordsValueSequence.size(); i++){
                    numpydata.list.add(0.0);
                }
                line.wordsLenSequence.forEach(w -> numpydata.list.add(new Double(w)));
                for (int i = 0; i < 4-line.wordsLenSequence.size(); i++){
                    numpydata.list.add(0.0);
                }

                String string = "";
                for (Double value : numpydata.list){
                    string = string + " " + value.toString();
                }
                string = string.substring(1,string.length());
                numpydataList.add(numpydata);
                writeIntoFiles.add(string);
            }

            resultList.add(temp);
        }

        FileOperator.putLinesToFile("test_data.txt", String.join("\n", writeIntoFiles));


        ResultDbService.getInstance().updateDCInformationFeatureOnChoice(resultList);
        //ResultDbService.getInstance().createTableDCInformationFeature();
        //ResultDbService.getInstance().insertDCInformationFeature(resultList);
    }

    public static int feature_one_numberOfWords(String mainwords){
        int result = 0;
        for (String string : mainwords.split(";")){
            if (!string.equals("")){
                result++;
            }
        }

        return result;
    }

    public static List<DCInformationStructure> feature_two_valueOfWords(List<DCInformationStructure> list){
        List<DCInformationStructure> result = new ArrayList<>();

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

            for (String string : line.wordsSequence){
                WordStructure info;
                info = words.get(string);

                info.wordsInEntry++;
                words.put(string, info);
            }
        }

        int numberOfEntry = list.size();
        for (DCInformationStructure line : list){
            for (String string : line.wordsSequence){
                WordStructure info;

                info = words.get(string);

                if (!info.calculated) {
                    info.TF = calculateTF(info.wordsInEntry, line.wordsSequence.size());
                    info.IDF = calculateIDF(numberOfEntry, info.entriesInDatabase);
                    info.TF_IDF = info.TF * info.IDF;
                    info.calculated = true;
                }

                words.put(string, info);
            }
        }

        for (DCInformationStructure line : list){
            for (String string : line.wordsSequence){
                line.wordsValueSequence.add(words.get(string).TF_IDF);
            }
        }

        result.addAll(list);

        return result;
    }

    public static double calculateTF(int x, int y){
        return (double)x/(double)y;
    }

    public static double calculateIDF(int x, int y){
        return Math.log10((double)x/(double)y);
    }

    public static List<Integer> feature_three_lenOfWords(List<String> wordsSequence){
        List<Integer> result = new ArrayList<>();

        for (String string : wordsSequence){
            result.add(string.length());
        }

        return result;
    }
}
