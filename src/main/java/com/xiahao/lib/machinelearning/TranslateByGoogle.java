package com.xiahao.lib.machinelearning;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.LanguageListOption;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.common.collect.ImmutableList;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

public class TranslateByGoogle {
    /**
     * Detect the language of input text.
     *
     * @param sourceText source text to be detected for language
     * @param out print stream
     */
    public static void detectLanguage(String sourceText, PrintStream out) {
        Translate translate = createTranslateService();
        List<Detection> detections = translate.detect(ImmutableList.of(sourceText));
        System.out.println("Language(s) detected:");
        for (Detection detection : detections) {
            out.printf("\t%s\n", detection);
        }
    }

    /**
     * Translates the source text in any language to English.
     *
     * @param sourceText source text to be translated
     * @param out print stream
     */
    public static void translateText(String sourceText, PrintStream out) {
        Translate translate = createTranslateService();
        Translation translation = translate.translate(sourceText);
        out.printf("Source Text:\n\t%s\n", sourceText);
        out.printf("Translated Text:\n\t%s\n", translation.getTranslatedText());
    }

    /**
     * Translate the source text from source to target language.
     * Make sure that your project is whitelisted.
     *
     * @param sourceText source text to be translated
     * @param sourceLang source language of the text
     * @param targetLang target language of translated text
     * @param out print stream
     */
    public static void translateTextWithOptionsAndModel(
            String sourceText,
            String sourceLang,
            String targetLang,
            PrintStream out) {

        Translate translate = createTranslateService();
        TranslateOption srcLang = TranslateOption.sourceLanguage(sourceLang);
        TranslateOption tgtLang = TranslateOption.targetLanguage(targetLang);

        // Use translate `model` parameter with `base` and `nmt` options.
        TranslateOption model = TranslateOption.model("nmt");

        Translation translation = translate.translate(sourceText, srcLang, tgtLang, model);
        out.printf("Source Text:\n\tLang: %s, Text: %s\n", sourceLang, sourceText);
        out.printf("TranslatedText:\n\tLang: %s, Text: %s\n", targetLang,
                translation.getTranslatedText());
    }


    /**
     * Translate the source text from source to target language.
     *
     * @param sourceText source text to be translated
     * @param sourceLang source language of the text
     * @param targetLang target language of translated text
     * @param out print stream
     */
    public static void translateTextWithOptions(
            String sourceText,
            String sourceLang,
            String targetLang,
            PrintStream out) {

        Translate translate = createTranslateService();
        TranslateOption srcLang = TranslateOption.sourceLanguage(sourceLang);
        TranslateOption tgtLang = TranslateOption.targetLanguage(targetLang);

        Translation translation = translate.translate(sourceText, srcLang, tgtLang);
        out.printf("Source Text:\n\tLang: %s, Text: %s\n", sourceLang, sourceText);
        out.printf("TranslatedText:\n\tLang: %s, Text: %s\n", targetLang,
                translation.getTranslatedText());
    }

    /**
     * Displays a list of supported languages and codes.
     *
     * @param out print stream
     * @param tgtLang optional target language
     */
    public static void displaySupportedLanguages(PrintStream out, Optional<String> tgtLang) {
        Translate translate = createTranslateService();
        LanguageListOption target = LanguageListOption.targetLanguage(tgtLang.orElse("en"));
        List<Language> languages = translate.listSupportedLanguages(target);

        for (Language language : languages) {
            out.printf("Name: %s, Code: %s\n", language.getName(), language.getCode());
        }
    }

    /**
     * Create Google Translate API Service.
     *
     * @return Google Translate Service
     */
    public static Translate createTranslateService() {
        return TranslateOptions.newBuilder().setApiKey("AIzaSyBYzE6nDBUeGIx449zIFm-AWtG2I1XZZwU").build().getService();
    }

    public static void main(String[] args) {
        String text = "http://www.kelai.com.tw/]@[車洋行有限公司KELAI ENTERPRISE CO.,LTD. – 專業汽車改裝零 ...;Hot Products. Audi · BMW · Toyota · MERCEDES BENZ · Lexus · Porsche. \n" +
                "Contact info. Your Name (required). Your Email (required). Your Message. 傳送中\n" +
                "... 車洋行有限公司KELAI ENTERPRISE CO.,LTD. Copyright © 2013 KELAI \n" +
                "Limited. 車洋行有限公司. All rights reserved. 製作維護點金網創意有限公司. \n" +
                "Google Translate ...]@[http://afld.kuas.edu.tw/?page_id=9230]@[謝文敏助理教授– KUAS,高雄應用科技大學應用外語系Department of ...;Education: Pennsylvania State University, Doctor of Philosophy. Courses: \n" +
                "English Conversation, English Writing, English for Hospitality Industry, English \n" +
                "Listening Comprehension for Science and Technology, Business English \n" +
                "Presentation, Second Language Acquisition. Area of Specialization: Computer-\n" +
                "Assisted ...]@[https://play.google.com/store/apps/details?id=hsd.hsd]@[淡江i生活- Android Apps on Google Play;淡江i生活專為淡江大學的學生及教職員設計的App，目前的功能有： 1.淡江Wi-Fi：\n" +
                "自動登入tku無線網路。 2.我是學生：有我的課表、考試小表、期中期末成績查詢和\n" +
                "課程異動。 3.我是老師：有我的授課表、修課學生(可察看學生資料的PDF檔)和課程\n" +
                "異動。 4.動態資訊：包含以下4個功能—— 即時影像：即時查看等公車或商館三樓電梯\n" +
                "的 ...]@[http://afld.kuas.edu.tw/?page_id=906]@[唐綺霞副教授– KUAS,高雄應用科技大學應用外語系Department of ...;期刊文章：. Tang, Chihsia. 2016. The Interplay of Cultural Expectation, Gender \n" +
                "Identity, and Communicative Behavior: Some Evidences from Compliment-\n" +
                "Responding Behavior. Pragmatics and Society (in press). [SSCI]; Tang, Chihsia. \n" +
                "2016. Managing criticisms in US-based and Taiwan-based reality talent contests: \n" +
                "A ...]@[https://play.google.com/store/apps/details?id=com.dtf.daanx]@[木棉手札(大安高工學生助理) - Android Apps on Google Play;2016年1月8日 ... 一個能將整個大安生活貫穿的App 從此再也不用忍受學校網站悲劇的界面從此再也\n" +
                "不用忍受IE only的成績查詢系統 特點： 1.安全 2.易用 3.佔用空間極小 功能介紹： 1.\n" +
                "各式公告查看經過重新縮排的界面與重新分類讓你能一眼找出你需要個公告 2.行事\n" +
                "曆快擺脫學校用pdf檔存的行事曆吧讓你知道原來行事曆查看可以 ...]@[http://afld.kuas.edu.tw/?page_id=9219]@[許呈瑟助理教授– KUAS,高雄應用科技大學應用外語系Department of ...;教師著作：. Education: National Kaohsiung First University of Science and \n" +
                "Technology, Doctor of Philosophy in Management; Courses: Business Software \n" +
                "Package, Financial English, International Communication for Business, Business \n" +
                "English Listening and Conversation, Technical English Writing, Reading \n" +
                "Strategies, ...]@[https://play.google.com/store/apps/details?id=kesshou.android.daanx]@[木棉手札2.0 - Android Apps on Google Play;一個能將整個大安生活貫穿的App 從此再也不用忍受學校網站悲劇的界面從此再也\n" +
                "不用忍受IE only的成績查詢系統 特點： 1.安全 2.易用 功能介紹： 1.各式公告查看\n" +
                "經過重新縮排的界面與重新分類讓你能一眼找出你需要個公告 2.行事曆快擺脫學校\n" +
                "用pdf檔存的行事曆吧讓你知道原來行事曆查看可以如此便利 3.獎懲紀錄與出勤紀錄\n" +
                "重新 ...]@[http://www.erlun.org.tw/index01.html]@[雲林縣二崙鄉農會全球資訊網;2015-中華民國104年果菜運銷股份有限公司--休市日預定表，詳情請參考中華民國\n" +
                "農會-年度休市日表。 > 1103, 行政院農業委員會農業藥物毒物試驗所舉辦「 .... 本會\n" +
                "家政班員陳錦華、潘品妃及高瑞螺參加縣農會舉辦之米穀粉創意競賽及在地米食推廣\n" +
                "整合行銷活動，榮獲第2名。 > 1206. 賀！本會農友-程永進先生參加雲林縣農事推廣\n" +
                " ...]@[https://www.facebook.com/sunny.lin.5815]@[Sunny Lin | Facebook;Sunny Lin is on Facebook. Join Facebook to connect with Sunny Lin and others \n" +
                "you may know. Facebook gives people the power to share and makes the world...]@[http://www.pixeldoeverything.com/]@[Pixel do Everything;福豐自動化. 福豐自動化股份有限公司成立於1979年，為一研發設計、製造配置全\n" +
                "方位機械自動控制設備，其整合控制經驗有：塑膠射出成型機、EVA 射出成型機、製\n" +
                "鞋機、橡膠機、塑膠... More info. WOODWISE Technology Co., Ltd. Thumbnail ...";

        TranslateByGoogle.translateText(text, System.out);
//        String command = args[0];
//        String text;

//        if (command.equals("detect")) {
//            text = args[1];
//            TranslateByGoogle.detectLanguage(text, System.out);
//        } else if (command.equals("translate")) {
//            text = args[1];
//            try {
//                String sourceLang = args[2];
//                String targetLang = args[3];
//                TranslateByGoogle.translateTextWithOptions(text, sourceLang, targetLang, System.out);
//            } catch (ArrayIndexOutOfBoundsException ex) {
//                TranslateByGoogle.translateText(text, System.out);
//            }
//        } else if (command.equals("langsupport")) {
//            try {
//                String target = args[1];
//                TranslateByGoogle.displaySupportedLanguages(System.out, Optional.of(target));
//            } catch (ArrayIndexOutOfBoundsException ex) {
//                TranslateByGoogle.displaySupportedLanguages(System.out, Optional.empty());
//            }
//        }
    }
}