package fdu.lab310.lib.analysis.extractConstring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class getPackageNameList {
    DBManager db = null;
    ResultSet rs = null;
    List<String> nameList = new ArrayList<>();


    public getPackageNameList(DBManager dbmanager) {
        super();
        db = dbmanager;
        rs = db.getPackageNameList();
        androidClassFilter();
    }

    public void androidClassFilter(){
        if(rs!= null){
            try {
                while (rs.next()){
                    String name = rs.getString("lib");
                    if(!name.contains("android.support")){
                        nameList.add(name);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}