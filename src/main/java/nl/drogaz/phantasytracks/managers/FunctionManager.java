package nl.drogaz.phantasytracks.managers;

import nl.drogaz.phantasytracks.Main;
import nl.drogaz.phantasytracks.objects.Track;
import nl.drogaz.phantasytracks.objects.TrackNode;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class FunctionManager {

    public static Boolean isRendering = false;
    private static int taskId;

    public static void displayTrackParticles(Player player, Track track) {
        player.sendMessage("Executing displayTrackParticles...");
        BukkitScheduler scheduler = Bukkit.getScheduler();
        taskId = scheduler.scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            player.sendMessage("Rendering track...");
            if (!Main.getInstance().getEnabledEditors().containsKey(player)) {
                player.sendMessage("Not in tha list!");
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }

            TrackManager trackManager = new TrackManager();
            List<TrackNode> nodes = trackManager.getNodes(track);

            if (nodes.size() < 2) {
                player.sendMessage("Track must have at least 2 nodes to render!");
                return;
            }

            int currentStep = 0;
            final int maxSteps = 35; // Adjust as needed for smoother/rougher interpolation

            for (int i = 0; i < nodes.size(); i++) {
                TrackNode cN = nodes.get(i);
                TrackNode nN = nodes.get(i + 1);

                Location currentNodeLoc = new Location(player.getWorld(), cN.getX(), cN.getY(), cN.getZ());
                Location nextNodeLoc = new Location(player.getWorld(), nN.getX(), nN.getY(), nN.getZ());

                player.spawnParticle(Particle.REDSTONE, currentNodeLoc, 5, 0, 0.1, 0.1, 0, new Particle.DustOptions(Color.GREEN, 1));

                for (int j = 0; j <= maxSteps; j++) {
                    double t = (double) j / maxSteps;
                    double x = currentNodeLoc.getX() * (1 - t) + nextNodeLoc.getX() * t;
                    double y = currentNodeLoc.getY() * (1 - t) + nextNodeLoc.getY() * t;
                    double z = currentNodeLoc.getZ() * (1 - t) + nextNodeLoc.getZ() * t;

                    // Spawn black particle along the interpolated path
                    player.spawnParticle(Particle.REDSTONE, new Location(currentNodeLoc.getWorld(), x, y, z), 1, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 1));
                }
            }
        }, 0, 1L);
    }

    public static Location getTrackLocation(Player player, TrackNode node) {
        return new Location(player.getWorld(), node.getX(), node.getY(), node.getX());
    }

}
