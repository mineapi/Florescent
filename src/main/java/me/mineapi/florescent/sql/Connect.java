package me.mineapi.florescent.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public static void connect() {
        Connection connection = null;
        try {
            String url = "jdbc:sqlite:./databases/bot.db";

            connection = DriverManager.getConnection(url);

            System.out.println("Connected to SQLite");
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }
        }
    }
}
