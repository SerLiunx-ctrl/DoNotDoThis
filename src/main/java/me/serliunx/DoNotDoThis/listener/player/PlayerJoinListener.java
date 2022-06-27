package me.serliunx.DoNotDoThis.listener.player;

import me.serliunx.DoNotDoThis.listener.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.generator.ChunkGenerator;

public class PlayerJoinListener extends Listener {

    public PlayerJoinListener() {
        super(false, true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
    }
}
