package com.xiahao.lib;

import java.util.HashSet;
import java.util.Set;

public class CreateWhiteList {
    public static Set<String> whiteList;
    public static Set<String> commonWords;
    public static Set<String> public_suffix_list;
    static {
        whiteList = new HashSet<>();
        whiteList.addAll(FileOperator.readFileByCharacter("helplist/whitelist"));

        commonWords = new HashSet<>();
        commonWords.addAll(FileOperator.readFileByCharacter("helplist/common-words"));

        public_suffix_list = new HashSet<>();
        public_suffix_list.addAll(FileOperator.readFileByCharacter("helplist/public_suffix_list"));
    }
}
