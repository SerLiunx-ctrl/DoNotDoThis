package me.serliunx.DoNotDoThis;

import me.serliunx.DoNotDoThis.listener.Listeners;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main plugin;
    private Listeners listeners;

    @Override
    public void onLoad(){
        plugin = this;
    }

    @Override
    public void onEnable(){
        listeners = new Listeners();
    }

    public static Main getPlugin() {
        return plugin;
    }

    public Listeners getListeners() {
        return listeners;
    }
}
