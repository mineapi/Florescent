package me.mineapi.florescent.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class MemberJoin extends ListenerAdapter { // Define class.
    // We don't need a constructor here because we don't need to instantiate any object-specific variables.

    // Define default settings.
    int welcomeEnabled = 0;

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        MessageChannel channel = null;
        String welcomeMessage = "";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./databases/bot.db");
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM settings WHERE guild = ?")) { // Get information from the database.
            ps.setLong(1, event.getGuild().getIdLong());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) { // If the row exists in the database, set welcomeEnabled to the value of the column.
                welcomeEnabled = rs.getInt("welcomeEnabled");
                channel = guild.getTextChannelById(rs.getLong("welcome_channel"));
                welcomeMessage = rs.getString("welcome_message");
            }
        } catch (SQLException throwables) { // If for whatever reason SQLite has an error, catch it here.
            throwables.printStackTrace();
        }

        if (welcomeEnabled == 1) {
            try {
                channel.sendMessage(welcomeMessage).queue();
            } catch (NullPointerException throwable) {
                System.out.println("Failed to send welcome message:" + throwable.getMessage());
                System.out.println("This is most likely due to improper configuration.");
            }
        }
    }
}
