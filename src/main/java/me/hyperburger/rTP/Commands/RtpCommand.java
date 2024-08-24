package me.hyperburger.rTP.Commands;

import me.hyperburger.rTP.API.RTPRequirements;
import me.hyperburger.rTP.API.RandomTeleportAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

public class RtpCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final RandomTeleportAPI randomTeleportAPI;

    public RtpCommand(final JavaPlugin plugin, final RandomTeleportAPI randomTeleportAPI) {
        this.plugin = plugin;
        this.randomTeleportAPI = randomTeleportAPI;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only a player can perform this command!");
            return false;
        }

        final Player player = (Player) commandSender;
        final World world = player.getWorld();

        player.sendMessage(ChatColor.GRAY + "Searching for a safe location to teleport...");

        RTPRequirements requirements = randomTeleportAPI.requirements(req -> {
            req.require(-10000, 10000, -10000, 10000);
        });

        randomTeleportAPI.location(world, requirements, true)
                .thenCompose(this::findSafeLocation)
                .thenAccept(location -> {
                    // Switch to the main thread for the final teleportation and messaging
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (location != null) {
                            player.teleport(location);
                            player.sendMessage(ChatColor.GREEN + "Teleported to a safe random location!");
                            player.sendMessage(ChatColor.GRAY + "Coordinates: " +
                                    ChatColor.WHITE + location.getBlockX() + ", " +
                                    location.getBlockY() + ", " + location.getBlockZ());
                        } else {
                            player.sendMessage(ChatColor.RED + "Failed to find a safe location. Please try again.");
                        }
                    });
                })
                .exceptionally(throwable -> {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.sendMessage(ChatColor.RED + "An error occurred while finding a location.");
                        throwable.printStackTrace();
                    });
                    return null;
                });

        return true;
    }

    private CompletableFuture<Location> findSafeLocation(Location initialLocation) {
        return CompletableFuture.supplyAsync(() -> {
            World world = initialLocation.getWorld();
            int x = initialLocation.getBlockX();
            int z = initialLocation.getBlockZ();

            for (int y = world.getMaxHeight(); y >= world.getMinHeight(); y--) {
                Location checkLocation = new Location(world, x, y, z);
                if (isSafeLocation(checkLocation)) {
                    return checkLocation;
                }
            }
            return null;
        });
    }

    private boolean isSafeLocation(Location location) {
        Block feet = location.getBlock();
        Block ground = feet.getRelative(0, -1, 0);
        Block head = feet.getRelative(0, 1, 0);

        return !feet.isLiquid()
                && !head.isLiquid()
                && feet.getType().isAir()
                && head.getType().isAir()
                && ground.getType().isSolid()
                && !isHazard(ground);
    }

    private boolean isHazard(Block block) {
        return block.getType() == Material.LAVA
                || block.getType() == Material.MAGMA_BLOCK
                || block.getType() == Material.CACTUS
                || block.getType() == Material.FIRE
                || block.getType() == Material.CAMPFIRE
                || block.getType() == Material.SOUL_CAMPFIRE;
    }
}