package me.serliunx.DoNotDoThis.listener.player;

import me.serliunx.DoNotDoThis.listener.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage("test");
    }
}
