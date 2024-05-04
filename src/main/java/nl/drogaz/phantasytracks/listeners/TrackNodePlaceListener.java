package nl.drogaz.phantasytracks.listeners;

import net.kyori.adventure.text.Component;
import nl.drogaz.phantasytracks.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class TrackNodePlaceListener implements Listener {

    @EventHandler
    public void onTrackNodePlace(PlayerInteractEvent event) {

        if (!event.getAction().isRightClick()) return;
        if (event.getItem() == null) return;
        if (event.getMaterial() != Material.STICK) return;
        if (event.getItem().displayName().equals(Component.text("Track Editor"))) return;
        if (!Main.getInstance().getEnabledEditors().containsKey(event.getPlayer())) return;




    }

}
