package com.xiahao.lib.machinelearning;

import com.xiahao.lib.FileOperator;

import java.util.ArrayList;
import java.util.List;

public class singleTest {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        list1.addAll(FileOperator.readFileByCharacter("C:\\Users\\xiahao\\Desktop\\MainWords"));
        list2.addAll(FileOperator.readFileByCharacter("C:\\Users\\xiahao\\Desktop\\URL"));

        List<String> mainwordsList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();

        mainwordsList.addAll(countWords.getWordsVector(String.join("", list1)));
        urlList.addAll(countWords.getWordsVector(String.join("", list2)));

        System.out.println(makeVector.getSimilarity(mainwordsList, urlList));
    }
}