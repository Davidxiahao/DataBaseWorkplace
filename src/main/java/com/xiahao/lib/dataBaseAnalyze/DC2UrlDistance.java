package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.OriginModel;
import com.xiahao.lib.CreateWhiteList;
import com.xiahao.lib.Url;
import com.xiahao.lib.machinelearning.DC2UrlSimilarity;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixListFactory;
import info.debatty.java.stringsimilarity.Levenshtein;

import java.util.ArrayList;
import java.util.List;

public class DC2UrlDistance {
    private static PublicSuffixListFactory factory = new PublicSuffixListFactory();
    private static PublicSuffixList suffixList = factory.build();
    public static void main(String[] args) {
        List<OriginModel> sourceData = DC2UrlSimilarity.getAllData();
        Levenshtein l = new Levenshtein();

        for (OriginModel line : sourceData) {
            if (line.isXSOP == 1) {
                boolean checkline = false;
                String keyword = "";
                double record = 0.0;
                for (String url : line.webOrigins.split(";")) {
                    if (url.contains("URL:")) {
                        url = url.replaceFirst("URL:", "");
                    }

                    Url singleUrl = new Url(url);
                    String host = singleUrl.getHost();

                    String suffix = suffixList.getPublicSuffix(host);
                    if (!CreateWhiteList.public_suffix_list.contains(suffix)) {
                        suffix = null;
                    }

                    int iFlag;
                    String keyWords;
                    if (suffix != null) {
                        iFlag = host.lastIndexOf(suffix);
                        StringBuffer stringBuffer = new StringBuffer(host);
                        if (iFlag != -1) {
                            stringBuffer = stringBuffer.replace(iFlag, iFlag + suffix.length(), "");
                            keyWords = stringBuffer.toString();
                        } else {
                            keyWords = host;
                        }
                    } else {
                        keyWords = host;
                    }

                    if (keyWords.equals("") && suffix != null) keyWords = suffix.split("\\.")[0];

                    List<String> templist = new ArrayList<>();
                    for (String string : keyWords.split("\\.")) {
                        if (!string.equals("")) {
                            templist.add(string);
                        }
                    }

                    if (line.keywords.size()>3) {
                        line.keywords = line.keywords.subList(line.keywords.size()-3, line.keywords.size());
                    }

                    boolean checkDC2Url = false;
                    for (String urlword : templist) {
                        for (String dcword : line.keywords){
                            String longString = dcword;
                            System.out.println(urlword+" "+dcword);
                            if (urlword.length()>longString.length())longString = urlword;
                            double distance = l.distance(urlword, dcword)/l.distance("", longString);
                            System.out.println(distance);
                            if (distance<0.3) {
                                checkDC2Url=true;
                                keyword = dcword;
                                record = 1-distance;
                            }
                        }
                    }
                    if (!checkDC2Url) {
                        checkline=true;
                    }
                }

                if (!checkline) {
                    line.isXSOP=0;
                    System.out.println(line.idx+" "+line.declaringClass+" "+line.webOrigins+" "+line.similarity+" "+line.keyWord);
                    System.out.println(keyword+" "+record);
                    line.similarity = record;
                    line.keyWord = keyword;
                }
            }
        }

        SampleDbService.getInstance().updatelast_origin_gp8w_meaningful(sourceData);
    }
}
