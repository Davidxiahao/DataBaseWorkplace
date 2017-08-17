package fdu.lab310.lib.analysis.extractConstring;

import soot.PackManager;
import soot.Transform;

/**
 * extract constant strings from an apk file
 */

public class extractConstring {
    /**
     * @param args apk file path
     */
    public static void main(String[] args) {
        DBManager db = new DBManager();

        Settings.initialiseSoot(args[0]);

        PackManager.v().getPack("jtp").add(new Transform("jtp.myAnalysis", new MyBodyTransformer(db)));
        System.out.println("sootTest start");
        PackManager.v().runPacks();
        System.out.println("sootTest end");
        db.closeConn();
    }
}