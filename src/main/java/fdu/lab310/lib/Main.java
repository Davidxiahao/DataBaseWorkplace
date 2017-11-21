package fdu.lab310.lib;

import com.hankz.util.dbService.LibsDbService;
import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.*;
import com.sun.javafx.collections.MappingChange;
import com.xiahao.lib.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.File;
import java.util.*;


/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
        List<OriginModel> input = new ArrayList<>();
        input.addAll(BuildWordList.getAllRecords());
        Map<String, Integer> summap = StringIDF.calculateStringFrequency(BuildWordList.fisrtWordList(input));
        Map<String, Double> map = StringIDF.calculateStringTFIDF(summap, input.size());

        map.remove("");
        for (String string : CreateWhiteList.whiteList){
            map.remove(string);
        }

        List<WordsIDFModel> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : map.entrySet()){
            result.add(new WordsIDFModel(entry.getKey(), entry.getValue(), summap.get(entry.getKey())));
        }

        Iterator<WordsIDFModel> iterator = result.iterator();
        while (iterator.hasNext()){
            WordsIDFModel word = iterator.next();
            if (word.sum < 10){
                iterator.remove();
            }
            else if (CreateWhiteList.commonWords.contains(word.word)){
                iterator.remove();
            }
            else if (word.word.contains(":") || isDigit(word.word)){
                iterator.remove();
            }
        }



        result.forEach(line -> System.out.println(line.word));

        //ResultDbService.getInstance().insertIntoWordsIDF(result);
        //ResultDbService.getInstance().insertIntoAfterRemove(result);
    }

    public static boolean isDigit(String strNum){
        return strNum.matches("[0-9]{1,}");
    }
}
