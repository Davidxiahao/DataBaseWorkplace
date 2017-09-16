package fdu.lab310.lib.analysis.LibsIdentifierFromDatabase;

public class Record {
    public String apk;
    public final String api;
    public final String webOrigins;
    public final String declareClass;

    public Record(String apk, String api, String webOrigins, String declareClass){
         this.apk = apk;
         this.api = api;
         this.webOrigins = webOrigins;
         this.declareClass = declareClass;
     }

    public void setApk(String apk){
        this.apk = apk;
    }

    public String getApk() {
        return apk;
    }

    public String getApi() {
        return api;
    }

    public String getWebOrigins() {
        return webOrigins;
    }

    public String getDeclareClass() {
        return declareClass;
    }

    public int hashCode(){
        int a = 7;
        int b = 10;
        int r= a;
        r = r*b + api.hashCode();
        r = r*b + webOrigins.hashCode();
        r = r*b + declareClass.hashCode();
        //System.out.println("apk: "+ apk +"; api: " + api + "; webOrigins: " + webOrigins + "; declareClass: " + declareClass + "; hashcode: " + r);
        return r;
    }


    public boolean equals(Object o){
        Record record = (Record)o;
        if(!apk.equals(record.getApk())) {
            //System.out.println("change apkName from " + apk + "to " + record.getApk());
            this.setApk(record.getApk());
            return true;
        }
        else{
            //System.out.println("Same apk");
            return true;
        }

    }

}
