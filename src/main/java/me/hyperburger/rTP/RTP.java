package me.hyperburger.rTP;

import me.hyperburger.rTP.API.RandomTeleportAPI;
import me.hyperburger.rTP.Commands.RtpCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class RTP extends JavaPlugin {
    private RandomTeleportAPI randomTeleportAPI;

    @Override
    public void onEnable() {
        this.randomTeleportAPI = new RTPAPIImpl();
        RandomTeleportAPI.initialize(this.randomTeleportAPI);

        // Plugin startup logic
        this.getCommand("rtp").setExecutor(new RtpCommand(this, randomTeleportAPI));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
