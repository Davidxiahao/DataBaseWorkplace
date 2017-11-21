package com.xiahao.lib;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.URLInformationModel;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixListFactory;

import java.util.*;

public class URLInformation {

    private static PublicSuffixListFactory factory = new PublicSuffixListFactory();
    private static PublicSuffixList suffixList = factory.build();

    public static void main(String[] args) {
        List<OriginModel> dataBase = new ArrayList<>();
        dataBase.addAll(OriginDbService.getInstance().getAllDataFromTable("last_origin_gp8w_meaningful"));

        Map<String, URLInformationStructure> result = new HashMap<>();
        for (OriginModel line : dataBase){
            if (!line.webOrigins.equals("")){
                for (String webString : line.webOrigins.split(";")){
                    Url url = new Url(webString);
                    URLInformationStructure info;
                    if (!result.containsKey(webString)){
                        info = new URLInformationStructure(webString);
                        String host = url.getHost();

                        String suffix = suffixList.getPublicSuffix(host);
                        if (!CreateWhiteList.public_suffix_list.contains(suffix)){
                            suffix = null;
                        }
                        String domain = suffixList.getRegistrableDomain(host);
                        if (domain == null) domain = host;

                        int iFlag;
                        String keyWords;
                        if (suffix != null){
                            iFlag = domain.lastIndexOf(suffix);
                            StringBuffer stringBuffer = new StringBuffer(domain);
                            if (iFlag != -1){
                                stringBuffer = stringBuffer.replace(iFlag, iFlag + suffix.length(), "");
                                keyWords = stringBuffer.toString();
                            }
                            else {
                                keyWords = domain;
                            }
                        }
                        else {
                            keyWords = domain;
                        }

                        if (keyWords.equals("") && suffix != null) keyWords = suffix.split("\\.")[0];

                        info.mainwords.addAll(Arrays.asList(keyWords.split("\\.")));
                    }
                    else {
                        info = result.get(webString);
                    }

                    info.total_frequence++;
                    info.APKs.add(line.apk);
                    info.DCs.add(line.declaringClass);
                    info.different_DC_frequence = info.DCs.size();
                    info.different_APK_frequence = info.APKs.size();

                    result.put(webString, info);
                }
            }
        }

        List<URLInformationModel> resultList = new ArrayList<>();
        for (Map.Entry<String, URLInformationStructure> entry : result.entrySet()){
            URLInformationStructure value = entry.getValue();
            URLInformationModel line = new URLInformationModel(
                    value.url,
                    String.join(";", value.mainwords),
                    value.total_frequence,
                    value.different_DC_frequence,
                    value.different_APK_frequence,
                    String.join(";", value.DCs),
                    String.join(";", value.APKs)
            );
            resultList.add(line);
        }

        ResultDbService.getInstance().createTableURLInformation();
        ResultDbService.getInstance().insertURLInformation(resultList);
    }
}
