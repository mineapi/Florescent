package me.mineapi.florescent.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDatabase {
    public static void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:./databases/" + fileName;

        try (Connection connection = DriverManager.getConnection(url)){
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created");
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
}
