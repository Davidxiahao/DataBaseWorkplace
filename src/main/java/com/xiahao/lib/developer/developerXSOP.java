package com.xiahao.lib.developer;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.DeveloperXSOPModel;
import com.hankz.util.dbutil.OriginModel;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.CreateWhiteList;
import com.xiahao.lib.Url;
import com.xiahao.lib.structure.DomainStructure;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixListFactory;

import java.lang.reflect.Array;
import java.util.*;

public class developerXSOP {
    private static PublicSuffixListFactory factory = new PublicSuffixListFactory();
    private static PublicSuffixList suffixList = factory.build();
    public static void main(String[] args) {
        List<OriginModel> meaningfulList = SampleDbService.getInstance().getAllDataFromlast_origin_gp8w_meaningful();
        Map<String, DomainStructure> domainMap = new HashMap<>();

        for (OriginModel line : meaningfulList){
            for (String url : line.webOrigins.split(";")){
                if (url.contains("URL:")){
                    url = url.replaceFirst("URL:", "");
                }

                Url singleUrl = new Url(url);
                String host = singleUrl.getHost();

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

                List<String> templist = new ArrayList<>();
                for (String string : keyWords.split("\\.")){
                    if (!string.equals("")){
                        templist.add(string);
                    }
                }

                String lastDomain;
                if (templist.size()==0) continue;
                else {
                    lastDomain = templist.get(templist.size()-1);
                }
                if (domainMap.containsKey(lastDomain)){
                    DomainStructure temp = domainMap.get(lastDomain);
                    if (!temp.apkList.contains(line.apk)){
                        temp.developerCounts.put(line.developers, temp.developerCounts.getOrDefault(line.developers, 1)+1);
                        temp.apkList.add(line.apk);
                    }
                    domainMap.put(lastDomain, temp);
                }
                else {
                    DomainStructure temp = new DomainStructure();
                    temp.apkList.add(line.apk);
                    temp.developerCounts.put(line.developers, 1);
                    domainMap.put(lastDomain, temp);
                }
            }
        }

        List<DeveloperXSOPModel> result = new ArrayList<>();
        int sum = 0;
        for (Map.Entry<String, DomainStructure> entry : domainMap.entrySet()){
            List<Map.Entry<String, Integer>> developerSet = new ArrayList<Map.Entry<String, Integer>>(entry.getValue().developerCounts.entrySet());

            developerSet.sort(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return (o2.getValue() - o1.getValue());
                }
            });

            sum++;
            result.add(new DeveloperXSOPModel(sum, entry.getKey(), developerSet.get(0).getKey(), 0, developerSet.get(0).getValue()));
            for (int i = 1; i < developerSet.size(); i++){
                sum++;
                result.add(new DeveloperXSOPModel(sum, entry.getKey(), developerSet.get(i).getKey(), 1, developerSet.get(i).getValue()));
            }
        }

        ResultDbService.getInstance().createTableDeveloperXSOP();
        ResultDbService.getInstance().insertDeveloperXSOP(result);
    }
}
