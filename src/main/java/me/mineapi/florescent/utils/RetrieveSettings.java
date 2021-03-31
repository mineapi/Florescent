package me.mineapi.florescent.utils;

import net.dv8tion.jda.api.entities.Guild;

import java.sql.*;

public class RetrieveSettings {
    private final Guild guild;

    //Establishes a connection to the database and updates guild.
    public RetrieveSettings(Guild guild) {
        this.guild = guild;
    }

    Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:./databases/bot.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public String getPrefix() {
        String prefix = "";
        String sql = "SELECT * FROM settings WHERE guild = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, this.guild.getIdLong());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                prefix = rs.getString("prefix");
            } else {
                prefix = "f!";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return prefix;
    }

    public boolean getWelcomeEnabled() {
        boolean welcomeEnabled = false;
        String sql = "SELECT * FROM settings WHERE guild = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, this.guild.getIdLong());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                if (rs.getInt("welcome_enabled") == 1) {
                    welcomeEnabled = true;
                } else if (rs.getInt("welcome_enabled") == 0) {
                    welcomeEnabled = false;
                }
            } else {
                welcomeEnabled = false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return welcomeEnabled;
    }

    public String getWelcomeMessage() {
        String welcomeMessage = "";
        String sql = "SELECT * FROM settings WHERE guild = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, this.guild.getIdLong());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                welcomeMessage = rs.getString("welcome_message");
            } else {
                welcomeMessage = "Welcome {user} to the server!";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return welcomeMessage;
    }

    public Long getWelcomeChannel() {
        Long welcomeChannel = 0L;
        String sql = "SELECT * FROM settings WHERE guild = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./databases/bot.db");
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, guild.getIdLong());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                welcomeChannel = rs.getLong("welcome_channel");
            } else {
                welcomeChannel = guild.getTextChannels().get(0).getIdLong();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return welcomeChannel;
    }

    public Long getAdminRole() {
        Long adminRole = 0L;
        String sql = "SELECT * FROM settings WHERE guild = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, this.guild.getIdLong());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                adminRole = rs.getLong("admin_role");
            } else {
                adminRole = 1L;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return adminRole;
    }
}
