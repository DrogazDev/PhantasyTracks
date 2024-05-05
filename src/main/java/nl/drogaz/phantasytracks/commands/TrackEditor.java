package nl.drogaz.phantasytracks.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import nl.drogaz.phantasytracks.Main;
import nl.drogaz.phantasytracks.libraries.ItemStackBuilder;
import nl.drogaz.phantasytracks.libraries.TrackManager;
import nl.drogaz.phantasytracks.objects.Track;
import nl.drogaz.phantasytracks.objects.TrackNode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@CommandAlias("track")
@CommandPermission("phantasytracks.track")
public class TrackEditor extends BaseCommand {

    @Default
    public void commandSyntax(Player player) {
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>/track create <name>"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>/track edit <name>"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>/track stopediting"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>/track delete <name>"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>/track list"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>If there are any issues, dm me on discord: <#00ff00>Drogaz"));
    }

    @Subcommand("create")
    public void createTrack(Player player, String[] args) {

        String trackName = args[0];
        String slug = trackName.toLowerCase().replace(" ", "_");

        try {

            List<TrackNode> nodes = new ArrayList<>();

            Track track = new Track(trackName, slug, nodes);
            Main.getInstance().getConfigManager().createCustomTrackConfig(trackName, track);

            player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>Track named " + args[0] + " has been created!"));

        } catch (Exception e) {
            player.sendMessage(e.getMessage());
            player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>There was something wrong while creating this track!"));
        }
    }

    @Subcommand("delete")
    public void deleteTrack(Player player, String[] args) {
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>Track named " + args[0] + " has been deleted!"));
    }

    @Subcommand("edit")
    @CommandCompletion("@tracks")
    public void editTrack(Player player, String[] args) {

        String trackName = args[0];
        Track track = new TrackManager().getTrackByName(trackName);

        if (Main.getInstance().getEnabledEditors().containsKey(player)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>You are already editing a track!"));
            return;
        }

        Main.getInstance().getEnabledEditors().put(player, track);
        player.getInventory().setItem(0, Main.getInstance().getTracktool());
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>Editing track named " + args[0] + "!"));
    }

    @Subcommand("stopediting")
    public void stopEditing(Player player) {
        if (!Main.getInstance().getEnabledEditors().containsKey(player)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>You are not editing any track!"));
            return;
        }

        Main.getInstance().getEnabledEditors().remove(player);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>Stopped editing the track!"));
    }

    @Subcommand("gettools")
    public void getTools(Player player) {
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>Here are the tools you need to create a track:"));
    }

    @Subcommand("list")
    public void listTracks(Player player) {
        List<Track> tracks = new TrackManager().getAllTracks();

        player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>All available tracks:"));
        tracks.forEach(track -> player.sendMessage(MiniMessage.miniMessage().deserialize("<#f0dc3c>> " + track.getName())));
    }
}
