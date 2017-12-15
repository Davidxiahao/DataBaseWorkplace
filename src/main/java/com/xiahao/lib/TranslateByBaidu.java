package com.xiahao.lib;

import com.xiahao.lib.com.baidu.translate.demo.TransApi;

public class TranslateByBaidu {
        private static final String APP_ID = "20171214000104777";
        private static final String SECURITY_KEY = "Mo25wDVEnU3dIBbgOW6B";

        public static void main(String[] args) {
            TransApi api = new TransApi(APP_ID, SECURITY_KEY);

            String query = "a謝文敏助理教授– KUAS,高雄應用科技大學應用外語系；淡江i生活- Android Apps on Google Play;淡江i生活專為淡江大學的學生及教職員設計的App，目前的功能有： 1.淡江Wi-Fi：自動登入tku無線網路。 2.我是學生：有我的課表、考試小表、期中期末成績查詢和課程異動。 3.我是老師：有我的授課表、修課學生(可察看學生資料的PDF檔)和課程異動。 4.動態資訊：包含以下4個功能—— 即時影像：即時查看等公車或商館三樓電梯的 ...";
            System.out.println(api.getTransResult(query, "auto", "en"));
        }
}
