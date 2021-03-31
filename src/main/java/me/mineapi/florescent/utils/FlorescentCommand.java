package me.mineapi.florescent.utils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;

import java.sql.SQLException;

public abstract class FlorescentCommand {
    // Enum for bot permissions, will be defined within each command.
    public enum FlorescentPermission { BOTOWNER, SERVERADMIN, SERVERMOD, SERVERMEMBER }

    private FlorescentPermission permissionLevel;

    public FlorescentCommand(FlorescentPermission permissions) {
        this.permissionLevel = permissions;
    }

    // Returns the name of the command, this the name that will be used to run the command.
    abstract public String name();

    // Returns the description of the command, this will be used in the help command.
    abstract public String description();

    // Runs the command's code.
    abstract public void execute(Message message, String[] args);
}
