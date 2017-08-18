package fdu.lab310.lib.analysis.extractConstring;

import java.sql.*;

public class DBManager {
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://10.141.209.138:6603/originchecker";
    String user = "originchecker";
    String pass = "originchecker";
    Connection connection = null;

    public DBManager() {
        super();
        ConnectDataBase();
    }

    public void ConnectDataBase(){
        try{
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connect successful!");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void closeConn(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean InsertString(String lib ,String s){
        boolean result = true;
        String sql = "INSERT INTO constring (lib, string) VALUE ('" + lib + "','" + s +"')";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);

        }
        catch (SQLException e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public ResultSet getPackageNameList(){
        ResultSet rs = null;
        String sql = "SELECT lib from libs";
        try{
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public boolean isExsit(String lib, String s){
        boolean result = false;
        String sql = "SELECT * from constring WHERE lib ='" + lib + "'and string ='" + s +"'";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                result = true;
            }

        }
        catch (SQLException e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
