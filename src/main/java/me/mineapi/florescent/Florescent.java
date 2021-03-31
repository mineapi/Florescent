package me.mineapi.florescent;

import io.github.cdimascio.dotenv.Dotenv;
import me.mineapi.florescent.handlers.EventHandler;
import me.mineapi.florescent.sql.Connect;
import me.mineapi.florescent.sql.CreateDatabase;
import me.mineapi.florescent.sql.CreateTable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.nio.file.Path;

public class Florescent {
    private static JDA api;
    private static EventHandler handler;
    public static void main(String[] args) throws LoginException {
        System.out.println("Loading bot...");

        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("BOT_TOKEN");
        api = JDABuilder.createDefault(token).build();

        CreateDatabase.createNewDatabase("bot.db");
        Connect.connect();
        CreateTable.createNewTable();
        handler = new EventHandler();

        System.out.println("Ready");
    }

    public static JDA getJDA() {
        return api;
    }

    public static EventHandler getEventHandler() {
        return handler;
    }
}
