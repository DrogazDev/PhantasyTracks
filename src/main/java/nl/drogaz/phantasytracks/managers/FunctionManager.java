package nl.drogaz.phantasytracks.managers;

import nl.drogaz.phantasytracks.Main;
import nl.drogaz.phantasytracks.libraries.ParticleUtils;
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
        BukkitScheduler scheduler = Bukkit.getScheduler();
        taskId = scheduler.scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            if (!Main.getInstance().getEnabledEditors().containsKey(player)) {
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }

            TrackManager trackManager = new TrackManager();
            List<TrackNode> nodes = trackManager.getNodes(track);

            if (nodes.size() < 2) {
                return;
            }

            int currentStep = 0;
            final int maxSteps = 15;

            for (int i = 0; i < nodes.size() - 1; i++) {
                TrackNode cN = nodes.get(i);
                TrackNode nN = nodes.get(i + 1);

                Location currentNodeLoc = new Location(player.getWorld(), cN.getX(), cN.getY(), cN.getZ());
                Location controlNodeLoc = new Location(player.getWorld(), cN.getX(), cN.getY() + 1, cN.getZ());
                Location nextNodeLoc = new Location(player.getWorld(), nN.getX(), nN.getY(), nN.getZ());

                player.spawnParticle(Particle.REDSTONE, currentNodeLoc, 5, 0, 0.1, 0.1, 0, new Particle.DustOptions(Color.GREEN, 1));

                // TODO: Update function later for more smoothness
                ParticleUtils.spawnSmoothCurveParticles(currentNodeLoc, nextNodeLoc, maxSteps, Particle.REDSTONE, Color.GRAY);
//                ParticleUtils.spawnQuadraticBezierCurveParticles(currentNodeLoc, controlNodeLoc, nextNodeLoc, maxSteps, Particle.REDSTONE, Color.GRAY);

            }
        }, 0, 1L);
    }

    public static Location getTrackLocation(Player player, TrackNode node) {
        return new Location(player.getWorld(), node.getX(), node.getY(), node.getX());
    }

}
