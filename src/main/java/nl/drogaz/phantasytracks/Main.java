package nl.drogaz.phantasytracks;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.Setter;
import nl.drogaz.phantasytracks.commands.TrackEditor;
import nl.drogaz.phantasytracks.libraries.ConfigManager;
import nl.drogaz.phantasytracks.objects.Track;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
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

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this, "config.yml");
        configManager.setup();

        registerConfigs();

        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new TrackEditor());

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
}
