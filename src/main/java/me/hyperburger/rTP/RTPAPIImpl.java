package me.hyperburger.rTP;

import me.hyperburger.rTP.API.RTPRequirements;
import me.hyperburger.rTP.API.RandomTeleportAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class RTPAPIImpl extends RandomTeleportAPI {

    @Override
    public void teleport(final Location location, final Player... players) {
        for (Player player : players) {
            player.teleport(location);
        }
    }

    @Override
    public CompletableFuture<Location> location(final World world, final RTPRequirements requirements, boolean inclusive) {
        return CompletableFuture.supplyAsync(() -> {
            int x = random(requirements.minX(), requirements.maxX(), inclusive);
            int z = random(requirements.minZ(), requirements.maxZ(), inclusive);
            int y = world.getHighestBlockYAt(x, z);
            return new Location(world, x, y, z);
        });
    }
}