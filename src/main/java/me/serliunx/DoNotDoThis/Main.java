package me.serliunx.DoNotDoThis;

import me.serliunx.DoNotDoThis.listener.Listeners;
import me.serliunx.DoNotDoThis.manager.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main plugin;
    private Listeners listeners;
    private ListenerManager listenerManager;

    @Override
    public void onLoad(){
        plugin = this;
    }

    @Override
    public void onEnable(){
        listeners = new Listeners();
        listenerManager = new ListenerManager();
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
}
