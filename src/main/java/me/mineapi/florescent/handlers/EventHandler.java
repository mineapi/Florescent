package me.mineapi.florescent.handlers;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.mineapi.florescent.Florescent;
import me.mineapi.florescent.events.MemberJoin;
import me.mineapi.florescent.events.MessageCreate;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class EventHandler {
    private final ArrayList<ListenerAdapter> listeners = new ArrayList<>();
    private static EventWaiter waiter;
    public EventHandler() {
        JDA api = Florescent.getJDA();

        System.out.println("Loading events...");

        initListeners();

        for (ListenerAdapter event : listeners) {
            api.addEventListener(event);
            System.out.println("Successfully loaded event " + event.getClass().getSimpleName() + "!");
        }

        waiter = new EventWaiter();

        api.addEventListener(waiter);
        System.out.println("Finished loading events.");
    }

    void initListeners() {
        listeners.add(new MessageCreate());
        listeners.add(new MemberJoin());
    }

    public EventWaiter getWaiter() {
        return waiter;
    }
}
