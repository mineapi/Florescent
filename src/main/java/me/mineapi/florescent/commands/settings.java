package me.mineapi.florescent.commands;

import me.mineapi.florescent.Florescent;
import me.mineapi.florescent.utils.FlorescentCommand;
import me.mineapi.florescent.utils.RetrieveSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.sql.*;
import java.util.Date;

public class settings extends FlorescentCommand {

    public settings() {
        super(FlorescentPermission.SERVERADMIN);
    }

    @Override
    public String name() {
        return "settings";
    }

    @Override
    public String description() {
        return "Change bot settings.";
    }

    @Override
    public void execute(Message message, String[] args) {
        String dbURL = "jdbc:sqlite:./databases/bot.db";

        try (Connection conn = DriverManager.getConnection(dbURL);
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM settings WHERE guild = ?")) {
            ps.setLong(1,message.getGuild().getIdLong());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                try {
                    if (args.length == 1) {
                        RetrieveSettings settings = new RetrieveSettings(message.getGuild());
                        String welcomeenabled = "";

                        if (settings.getWelcomeEnabled()) {
                            welcomeenabled = "Yes";
                        } else if (!settings.getWelcomeEnabled()) {
                            welcomeenabled = "No";
                        }

                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("Settings")
                                .setDescription("The following are settings for this guild.")
                                .setThumbnail(message.getGuild().getIconUrl())
                                .addField("Prefix", settings.getPrefix(), false)
                                .addField("Welcome Enabled", welcomeenabled, false)
                                .addField("Welcome Message", settings.getWelcomeMessage(), false)
                                .addField("Welcome Channel", Long.toString(settings.getWelcomeChannel()), false)
                                .addField("Admin Role", Long.toString(settings.getAdminRole()), false)
                                .setColor(Color.MAGENTA)
                                .setTimestamp(new Date().toInstant())
                                .setFooter("Made by MineAPI", Florescent.getJDA().getSelfUser().getAvatarUrl());
                        message.getChannel().sendMessage(embed.build()).queue();
                    } else if (args[1].equals("prefix")) {
                        RetrieveSettings settings = new RetrieveSettings(message.getGuild());
                        PreparedStatement ps1 = conn.prepareStatement("UPDATE settings SET prefix = ? WHERE guild = ?");
                        ps1.setString(1, args[2]);
                        ps1.setLong(2, message.getGuild().getIdLong());
                        ps1.executeUpdate();

                        message.getChannel().sendMessage("Set bot prefix to " + settings.getPrefix()).queue();
                    } else if (args[1].equals("welcomeenabled")) {
                        RetrieveSettings settings = new RetrieveSettings(message.getGuild());
                        PreparedStatement ps1 = conn.prepareStatement("UPDATE settings SET welcome_enabled = ? WHERE guild = ?");
                        if (args.length == 3) {
                            if (args[2].equals("yes")) {
                                ps1.setInt(1, 1);
                                ps1.setLong(2, message.getGuild().getIdLong());
                                ps1.executeUpdate();
                                message.getChannel().sendMessage("Members will now receive welcome messages!").queue();
                            } else if (args[2].equals("no")) {
                                ps1.setInt(1, 0);
                                ps1.setLong(2, message.getGuild().getIdLong());
                            } else {
                                message.getChannel().sendMessage("Invalid result, you may only set welcomeEnabled to yes or no!").queue();
                            }
                        } else {
                            message.getChannel().sendMessage("Invalid usage, please use " + settings.getPrefix() + "settings welcomeenabled [yes/no]!").queue();
                        }
                    } else if (args[1].equals("welcomemessage")) {
                        RetrieveSettings settings = new RetrieveSettings(message.getGuild());
                        PreparedStatement ps1 = conn.prepareStatement("UPDATE settings SET welcome_message = ? WHERE guild = ?");

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < args.length; i++) {
                            if (args[i].equals(settings.getPrefix() + "settings") || args[i].equals("welcomemessage")) {
                                i++;
                                i++;
                            }
                            result.append(args[i] + " ");
                        }

                        ps1.setString(1, result.toString());
                        ps1.setLong(2, message.getGuild().getIdLong());
                        ps1.executeUpdate();

                        message.getChannel().sendMessage("Set the welcome message to: `" + settings.getWelcomeMessage() + "`!").queue();
                    } else if (args[1].equals("welcomechannel")) {
                        RetrieveSettings settings = new RetrieveSettings(message.getGuild());
                        PreparedStatement ps1 = conn.prepareStatement("UPDATE settings SET welcome_channel = ? WHERE guild = ?");

                        try {
                            ps1.setLong(1, message.getMentionedChannels().get(0).getIdLong());
                            ps1.setLong(2, message.getGuild().getIdLong());
                            ps1.executeUpdate();
                            message.getChannel().sendMessage("Set welcome channel to " + settings.getWelcomeChannel() + "!").queue();
                        } catch (IndexOutOfBoundsException e) {
                            message.getChannel().sendMessage("Invalid channel provided, please mention a channel!").queue();
                        }
                    } else if (args[1].equals("adminrole")) {
                        RetrieveSettings settings = new RetrieveSettings(message.getGuild());
                        PreparedStatement ps1 = conn.prepareStatement("UPDATE settings SET admin_role = ? WHERE guild = ?");
                        try {
                            ps1.setLong(1, message.getMentionedRoles().get(0).getIdLong());
                            ps1.setLong(2, message.getGuild().getIdLong());
                            ps1.executeUpdate();
                            message.getChannel().sendMessage("Set admin role to " + settings.getAdminRole() + "!").queue();
                        } catch (IndexOutOfBoundsException e) {
                            message.getChannel().sendMessage("Invalid role, please mention the role!").queue();
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                PreparedStatement ps2 = conn.prepareStatement("INSERT INTO settings(guild, prefix, welcome_enabled, welcome_message, welcome_channel, admin_role) VALUES(?, ?, ?, ?, ?, ?)");
                ps2.setLong(1, message.getGuild().getIdLong());
                ps2.setString(2, "f!");
                ps2.setInt(3, 0); // Default welcomeEnabled to false.
                ps2.setString(4, "Welcome {user} to the server!");
                ps2.setLong(5, message.getGuild().getChannels().get(0).getIdLong()); // Set the welcome channel to the first channel the bot can see.
                ps2.setLong(6, 123456789101213141L); // This is a temp number, you must define this before admins can start using admin features!
                ps2.executeUpdate();

                message.getGuild().retrieveMember(Florescent.getJDA().getSelfUser()).queue();
                EmbedBuilder eb = new EmbedBuilder()
                        .setDescription("I couldn't find settings for this guild so I created them for you, rerun the command.")
                        .setColor(Color.MAGENTA);
                message.getChannel().sendMessage(eb.build()).queue();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}