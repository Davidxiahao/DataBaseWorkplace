package com.hankz.util.dbutil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by HankZhang on 2017/7/7.
 */
@FunctionalInterface
public interface StatementPreparor {
    void prepare(PreparedStatement ps) throws SQLException;
}

