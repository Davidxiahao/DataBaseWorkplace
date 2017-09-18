package com.xiahao.lib;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FindMIX {
    private static Pattern DC_Pattern = Pattern.compile("\\[DC\\]((\\w+)(\\.(\\w|\\$)+)*)(;|$|\\s)");
    public static boolean isMixed(String codeOrigins){

        Matcher matcher = DC_Pattern.matcher(codeOrigins);
        String dc = matcher.find() ? matcher.group() : "NOT_FOUND";
        if (!dc.equals("NOT_FOUND")) {
            dc = dc.split("\\[DC]")[1];
            for (String string : dc.split("\\.")) {
                if (!PredefinedList.ignoredWordSetInWebHost.contains(string) && string.length() > 3) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
