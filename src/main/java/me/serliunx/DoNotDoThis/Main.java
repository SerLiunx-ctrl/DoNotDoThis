package me.serliunx.DoNotDoThis;

import me.serliunx.DoNotDoThis.command.Commands;
import me.serliunx.DoNotDoThis.listener.Listeners;
import me.serliunx.DoNotDoThis.manager.CommandManager;
import me.serliunx.DoNotDoThis.manager.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main plugin;
    private Listeners listeners;
    private ListenerManager listenerManager;
    private Commands commands;
    private CommandManager commandManager;

    @Override
    public void onLoad(){
        plugin = this;
    }

    @Override
    public void onEnable(){
        listeners = new Listeners();
        listenerManager = new ListenerManager();
        commands = new Commands();
        commandManager = new CommandManager("donotdothis");
    }

    public static Main getPlugin() {
        return plugin;
    }

    public Listeners getListeners() {
        return listeners;
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public Commands getCommands() {
        return commands;
    }
}
