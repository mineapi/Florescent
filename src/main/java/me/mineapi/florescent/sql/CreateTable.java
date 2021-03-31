package me.mineapi.florescent.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void createNewTable() {
        String url = "jdbc:sqlite:./databases/bot.db";

        String sql = "CREATE TABLE IF NOT EXISTS settings (\n" +
                "   id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "   guild long NOT NULL,\n" +
                "   prefix text NOT NULL,\n" +
                "   welcome_enabled integer NOT NULL,\n" +
                "   welcome_message text NOT NULL,\n" +
                "   welcome_channel long NOT NULL,\n" +
                "   admin_role long NOT NULL\n" +
                ");";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
}
