package com.xiahao.lib.machinelearning;

import com.xiahao.lib.FileOperator;

import java.util.ArrayList;
import java.util.List;

public class singleTest {
    public static void main(String[] args) {

        List<String> list1 = new ArrayList<>(FileOperator.readFileByCharacter("C:\\Users\\xiahao\\Desktop\\MainWords"));
        List<String> list2 = new ArrayList<>(FileOperator.readFileByCharacter("C:\\Users\\xiahao\\Desktop\\URL"));

        List<String> mainwordsList = new ArrayList<>(countWords.getWordsVector(String.join("", list1)));
        List<String> urlList = new ArrayList<>(countWords.getWordsVector(String.join("", list2)));

        System.out.println(makeVector.getSimilarity(mainwordsList, urlList));
    }
}