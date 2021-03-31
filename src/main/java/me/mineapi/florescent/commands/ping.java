package me.mineapi.florescent.commands;

import me.mineapi.florescent.utils.FlorescentCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Date;

public class ping extends FlorescentCommand {
    public ping() {
        super(FlorescentPermission.SERVERMEMBER);
    }

    @Override
    public String name() {
        // REMEMBER, NO CAPITALIZATION IS ALLOWED HERE!!!
        return "ping";
    }

    @Override
    public String description() {
        return "Receive bot latency.";
    }

    @Override
    public void execute(Message message, String[] args) {
        MessageChannel channel = message.getChannel();

        long currentTime = new Date().getTime();

        Message pingMessage = channel.sendMessage("Please wait...").complete();

        long elapsedTime = new Date().getTime() - currentTime;

        pingMessage.editMessage("Bot ping is " + elapsedTime + "ms").queue();
    }
}
