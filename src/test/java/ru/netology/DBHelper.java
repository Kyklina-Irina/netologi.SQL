package ru.netology;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DBHelper {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/app";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "pass";

    private static DataSource createDataSource() {
        org.postgresql.ds.PGSimpleDataSource dataSource = new org.postgresql.ds.PGSimpleDataSource();
        dataSource.setUrl(DB_URL);
        dataSource.setUser(DB_USER);
        dataSource.setPassword(DB_PASSWORD);
        return dataSource;
    }

    public static String getVerificationCode(String login) throws SQLException {
        QueryRunner runner = new QueryRunner(createDataSource());
        String sql = "SELECT code FROM auth_codes WHERE user_id = (SELECT id FROM users WHERE login = ?) ORDER BY created DESC LIMIT 1";
        return runner.query(sql, new ScalarHandler<>(), login);
    }
}