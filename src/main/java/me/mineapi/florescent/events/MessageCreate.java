package me.mineapi.florescent.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import me.mineapi.florescent.commands.ping;
import me.mineapi.florescent.commands.settings;
import me.mineapi.florescent.utils.FlorescentCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;

public class MessageCreate extends ListenerAdapter { // Define class.
    private final ArrayList<FlorescentCommand> commands = new ArrayList<>(); // Create a list of commands so we can loop through them later.
    public MessageCreate() { // Create constructor that can be called when we load the event into the JDA.
        System.out.println("Loading commands...");

        initCommands(); // Instantiate the list of

        for (FlorescentCommand command : commands) {
            System.out.println("Loaded command " + command.name() + "!");
        }
    }

    // Default bot settings.
    String defaultPrefix = "f!";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) { // Actual message event.
        String prefix = defaultPrefix;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./databases/bot.db");
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM settings WHERE guild = ?")) {
            ps.setLong(1, event.getGuild().getIdLong());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) { // If the row in the database exists, set the value of prefix to the one defined in the database.
                prefix = rs.getString("prefix");
            }
        } catch (SQLException throwables) { // Throw an exception if for whatever reason SQLite hecks out.
            throwables.printStackTrace();
        }

        if (!event.getAuthor().isBot()) {
            for (FlorescentCommand command : commands) {
                String[] args = event.getMessage().getContentRaw().split(" ");
                if (args[0].equals(prefix + command.name().toLowerCase())) {
                    command.execute(event.getMessage(), args);
                    System.out.println("User " + event.getAuthor().getAsTag() + ", with an ID of " + event.getAuthor().getId() + " executed the command " + command.name() + ".");
                }
            }
        }
    }

    void initCommands() {
        commands.add(new ping());
        commands.add(new settings());
    }
}
