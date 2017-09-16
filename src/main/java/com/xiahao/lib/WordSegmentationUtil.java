package com.xiahao.lib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by HankZhang on 2017/9/16.
 */
public class WordSegmentationUtil {
    /***
     *
     * @param targetStr webOrigins to be segmented.
     * @param helperStr package name from codeOrigins to help the segmentation.
     * @return
     */
    public static Set<String> segmentWord(List<String> targetStr, String helperStr) {
        Set<String> result = new HashSet<>();
        result.addAll(targetStr);
        return result;
    }
}