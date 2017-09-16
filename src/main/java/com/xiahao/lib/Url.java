package com.xiahao.lib;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by HankZhang on 2017/9/8.
 */
public class Url {

    private String protocol;   //default ""
    private String host;       //default the string itself
    private int port;          //default -1
    private String path;       //default ""

    private  String origStr;

    public Url(String origStr){
        this.origStr = origStr;
        parseUrl(origStr);
    }


    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }


    @Override
    public String toString() {
        return "Url{" +
                "protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                '}';
    }

    public void parseUrl(String origStr) {
        // pre ad hoc fix
        origStr = preAdHocFix(origStr);

        // check shortUrl, e.g. http://goo.gl/h6TlrA
        origStr = DomainIPUtil.checkAndParseShortUrl(origStr);

        int idx;
        // 1. split the protocol from others, default is http
        String protocol = "http";

        // a). http://www.example.com/path?arg=xxx;  b). tel:51355355, a.com:8080, ????
        if ((idx = origStr.indexOf("://")) >= 0) {
            protocol = origStr.substring(0, idx);
            origStr = origStr.substring(idx + 3, origStr.length());
        }

        // 2. split the host from the path
        String host = "", path = "";
        // a). www.example.com/path/to    b). www.quran-now.com?showbar=false     c). www.quran-now.com#bottom
        if ((idx = origStr.indexOf("/")) >= 0 || (idx = origStr.indexOf("?")) >= 0 || (idx = origStr.indexOf("#")) >= 0) {
            host = origStr.substring(0, idx);
            path = origStr.substring(idx + 1, origStr.length());
        } else {
            Matcher matcher = hostPattern.matcher(origStr);
            if(matcher.find()){
                host = matcher.group();
                path = origStr.substring(matcher.end());
            }
        }

        // If it ends with these suffix, then it should be path. e.g. Read_PDF.aspx, why.me.html
        for (String suffix : PredefinedList.SUFFIX) {
            if (host.endsWith("." + suffix)) {
                host = "";
                path = origStr;
            }
        }

        // get the port
        int port = -1;
        if ((idx = host.indexOf(":")) >= 0) {
            try {
                if (idx < host.length())
                    port = Integer.parseInt(host.substring(idx + 1, host.length()));
                host = host.substring(0, idx);
            } catch (NumberFormatException e) {
                System.err.println("Error paring url port: " + origStr);
            }
        }

        // lower case & trim
        this.protocol = protocol.toLowerCase().trim();
        this.host = host.toLowerCase().trim();
        this.port = port;
        this.path = path.toLowerCase().trim();

        // do some check below
        // 1. check IP
        if (DomainIPUtil.isAllNum(this.host)) {
            if (DomainIPUtil.isIPAddr(this.host)) {
                this.host = DomainIPUtil.getDomainFromIP(this.host);
            } else{
                this.host = "";
            }
        }

        // 2. ad hoc
        adHocFix();
    }

    private String preAdHocFix(String origStr) {
        // pre host fix for: URL:https://payment.magzter.com/android/index.php?user_id=&mag_id=&issue_id=&duration=&price=null&is_newstand=1&currency=null&cc_code=&local_cur=&local_price=
        // this is due to our detection tool...
        if(origStr.startsWith("URL:")){
            origStr = origStr.substring(4);
        }
        return origStr;
    }

    private void adHocFix(){
        // fix "google.streetview:", which will be parsed as host=google.streatview
        if (this.getHost().equalsIgnoreCase("google.streetview")) {
            this.protocol = "google.streetview";
            this.host = "";
        }

        // 152 app, 453 records contains "adc_bridge.device_type=phone" from onPageFinished()
        if (this.getHost().contains("adc_bridge.device_type")) {
            this.protocol = "";
            this.host = "";
        }

    }


    static List<String> acceptedProtocol = Arrays.asList("http", "https");
    static Pattern hostPattern = Pattern.compile("[\\w\\-_\\.]+(\\.[\\w\\-_]+)+");

    public boolean isMeaningfulUrl(){
//        System.out.println(this.toString());
        if (!acceptedProtocol.contains(this.getProtocol())) {
            return false;
        }
        if (!hostPattern.matcher(this.getHost()).matches()) {
            return false;
        }
        return true;
    }


    public List<String> getIdentitiesOfHost(){
        // TODO: 2017/9/16  use word segmentation
        return Stream.of(this.host.split("\\."))
                .filter(word -> !PredefinedList.ignoredWordSetInWebHost.contains(word) && word.length() > 2)
                .collect(Collectors.toList());
    }



    private static void testData() {
        List<String> testList = Arrays.asList(
                "http://www.example.com:8080/path/a.html?a=1&b=2#top",
                "http://www.example.com/path/a.html?a=1&b=2#top",
                "http://www.example.com/path/a.html?a=1&b=2",
                "http://www.example.com/path/a.html?a=1&b=2#top",
                "http://www.example.com/path/a.html#top",
                "http://www.example.com/path#top",
                "http://www.example.com/",
                "www.example.com",
                "www.example.com:1314", // TODO: 2017/9/15 wrong path and port, maybe need adhoc fix.
                ".facebook.com",
                "google.streetview:",
                "jp.naver.line.android",
                "pkg=jp.naver.line.android",
                "buy.aspx?a=1",
                "a.buy.aspx",
                "a.buy.aspx?a=1",
                "a.b.c.html",
                "a.com/hello.html"
        );

        testList.forEach(t -> System.out.println(new Url(t).isMeaningfulUrl()));
    }


}
