package nl.drogaz.phantasytracks.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import nl.drogaz.phantasytracks.Main;
import nl.drogaz.phantasytracks.libraries.PersistentTags;
import nl.drogaz.phantasytracks.managers.TrackManager;
import nl.drogaz.phantasytracks.objects.TrackNode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class TrackNodePlaceListener implements Listener {

    @EventHandler
    public void onTrackNodePlace(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().isRightClick()) return;
        if (event.getItem() == null) return;
        if (event.getMaterial() != Material.BLAZE_ROD) return;
        if (!PersistentTags.checkData("phantasyland", "trackeditor", event.getItem().getItemMeta())) return;
        if (!Main.getInstance().getEnabledEditors().containsKey(event.getPlayer())) return;
        event.setCancelled(true);

        TrackManager trackManager = new TrackManager();
        Location location = player.getLocation();

        try {
            TrackNode trackNode = new TrackNode(location.x(), location.y(), location.z());
            trackManager.addNodeToTrack(Main.getInstance().getEnabledEditors().get(player), trackNode);
            player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>Track node placed at " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()));
        } catch(Exception e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>There was an error while placing the track node!"));
        }
    }

}
