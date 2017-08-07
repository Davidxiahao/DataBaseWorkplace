package com.hankz.util.dbutil;

import java.sql.*;

/**
 * Created by HankZhang on 2017/8/7.
 */
public class DbHelper {
    private final String Class_Name = "org.sqlite.JDBC";
    private final String DB_URL;

    public DbHelper(String db_url) {
        this.DB_URL = db_url;
    }

    /**
     * Create a connection
     * @return
     */
    public Connection createConnection() {
        Connection conn = null;
        try {
            Class.forName(Class_Name);
            conn = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    /***
     * execute one update
     * @param sql
     * @return
     */
    public  boolean doUpdate(String sql) {
        try (Connection conn = createConnection();
             Statement stmt = conn.createStatement())
        {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * update using PreparedStatement.
     * @param sql
     * @param preparor
     * @return
     */
    public boolean doUpdate(String sql, StatementPreparor preparor) {
        try (Connection conn = createConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            preparor.prepare(ps);
            ps.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /***
     * do batch update
     * @param sql
     * @param preparor
     * @return
     */
    public boolean doBatchUpdate(String sql, StatementPreparor preparor) {
        try(Connection conn = createConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            preparor.prepare(ps);
            conn.setAutoCommit(false);
            int [] ret = ps.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /***
     *
     * @param sql
     * @param processor
     */
    public void doQuery(String sql, ResultProcessor processor) {
        try(Connection conn = createConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            processor.process(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doQuery(String sql, StatementPreparor preparor, ResultProcessor processor) {
        try(Connection conn = createConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            preparor.prepare(ps);
            ResultSet rs = ps.executeQuery();
            processor.process(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
