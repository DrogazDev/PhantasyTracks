package nl.drogaz.phantasytracks;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.Setter;
import nl.drogaz.phantasytracks.commands.TrackEditor;
import nl.drogaz.phantasytracks.managers.ConfigManager;
import nl.drogaz.phantasytracks.libraries.ItemStackBuilder;
import nl.drogaz.phantasytracks.managers.TrackManager;
import nl.drogaz.phantasytracks.listeners.TrackNodePlaceListener;
import nl.drogaz.phantasytracks.objects.Track;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private PaperCommandManager commandManager;

    @Getter
    @Setter
    private Map<Player, Track> enabledEditors = new HashMap<>();

    @Getter
    @Setter
    private ConfigManager configManager;

    @Getter
    private ItemStack tracktool;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this, "config.yml");
        configManager.setup();

        registerConfigs();

        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new TrackEditor());

        registerListeners();

        List<String> lore = new ArrayList<>();
        lore.add("§7> Click to add a node");
        lore.add("§7> Right click to remove a node");
        lore.add("§7> Shift + right click to select a node");

        tracktool = new ItemStackBuilder(Material.BLAZE_ROD).setName("§6Track Editor")
                .addLores(lore)
                .addGlow()
                .build();

        ItemMeta meta = tracktool.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(this, "phantasyland"), PersistentDataType.STRING, "trackeditor");
        tracktool.setItemMeta(meta);

        List<Track> tracks = new TrackManager().getAllTracks();
        this.commandManager.getCommandCompletions().registerAsyncCompletion("tracks", c -> {
            List<String> list = new ArrayList<>();
            for (Track track : tracks) {
                String name = track.getName();
                list.add(name);
            }
            return list;
        });
    }

    @Override
    public void onDisable() {
        configManager.saveMainConfig();
    }

    private void registerConfigs() {
        // Register main config
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Register all configs in the "tracks" folder
        File tracksFolder = new File(getDataFolder(), "tracks");
        if (tracksFolder.exists() && tracksFolder.isDirectory()) {
            File[] trackFiles = tracksFolder.listFiles();
            if (trackFiles != null) {
                for (File trackFile : trackFiles) {
                    if (trackFile.isFile() && trackFile.getName().endsWith(".yml")) {
                        FileConfiguration trackConfig = YamlConfiguration.loadConfiguration(trackFile);
                        configManager.getMainConfig().addDefaults(trackConfig);
                    }
                }
            }
        }
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new TrackNodePlaceListener(), this);
    }
}
