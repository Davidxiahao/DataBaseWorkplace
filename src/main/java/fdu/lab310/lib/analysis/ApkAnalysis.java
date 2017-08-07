package fdu.lab310.lib.analysis;

/**
 * Created by HankZhang on 2017/8/7.
 */
public class ApkAnalysis implements IAnalysis {
    private String apkPath;

    public ApkAnalysis(String apkPath) {
        this.apkPath = apkPath;
    }


    @Override
    public void doAnalysis() {
        System.out.println("Analyzing " + this.apkPath);

    }
}
