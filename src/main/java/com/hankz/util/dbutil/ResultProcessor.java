package com.hankz.util.dbutil;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by HankZhang on 2017/8/7.
 */
@FunctionalInterface
public interface ResultProcessor{
    void process(ResultSet rs) throws SQLException;
}