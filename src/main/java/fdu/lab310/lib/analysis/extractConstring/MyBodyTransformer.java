package fdu.lab310.lib.analysis.extractConstring;

import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeStmt;
import soot.jimple.StringConstant;

import java.util.Iterator;
import java.util.Map;

public class MyBodyTransformer extends BodyTransformer {
    DBManager db ;
    getPackageNameList packageNameList;

    public MyBodyTransformer(DBManager dbManager) {
        super();
        db = dbManager;
        packageNameList = new getPackageNameList(dbManager);
    }

    @Override
    protected void internalTransform(Body body, String arg0, Map arg1) {
        String Class = body.getMethod().getDeclaringClass().getName().toString();
        int last = Class.lastIndexOf(".");
        String packageName = Class.substring(0, last);

        if(packageNameList.nameList.contains(packageName)) {
            Iterator<Unit> i = body.getUnits().snapshotIterator();
            while (i.hasNext()) {
                Unit u = i.next();
                if (u instanceof InvokeStmt) {
                    InvokeStmt invoke = (InvokeStmt) u;
                    int count = invoke.getInvokeExpr().getArgCount();
                    if (count > 0) {
                        for (int j = 0; j < count; j++) {
                            Value value = invoke.getInvokeExpr().getArg(j);
                            if (value instanceof StringConstant) {
                                String s = value.toString().replaceAll("\"", "");
                                if(s == null||s.length()<=0){
                                }
                                if(s.contains("\\")){
                                }
                                else {
                                    if(!db.isExsit(packageName, s)) {
                                        db.InsertString(packageName, s);
                                        System.out.println("String Found: " + packageName + ":" + value.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
