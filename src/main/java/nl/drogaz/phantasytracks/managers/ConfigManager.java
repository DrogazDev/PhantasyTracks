package nl.drogaz.phantasytracks.managers;

import lombok.Getter;
import lombok.Setter;
import nl.drogaz.phantasytracks.objects.Track;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration mainConfig;
    private File mainConfigFile;
    private final String mainFileName;
    private File tracksFolder;

    public ConfigManager(JavaPlugin plugin, String mainFileName) {
        this.plugin = plugin;
        this.mainFileName = mainFileName;
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        mainConfigFile = new File(plugin.getDataFolder(), mainFileName);
        if (!mainConfigFile.exists()) {
            plugin.saveResource(mainFileName, false);
        }
        mainConfig = plugin.getConfig();

        // Create a "tracks" folder if it doesn't exist
        tracksFolder = new File(plugin.getDataFolder(), "tracks");
        if (!tracksFolder.exists()) {
            tracksFolder.mkdir();
        }
    }

    public void saveMainConfig() {
        try {
            mainConfig.save(mainConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadMainConfig() {
        mainConfig = plugin.getConfig();
    }

    // Function to create a custom track configuration file
    public void createCustomTrackConfig(String trackName, Track track) throws IOException {
        File customTrackConfigFile = new File(tracksFolder, trackName + ".yml");
        customTrackConfigFile.createNewFile();
        FileConfiguration customTrackConfig = YamlConfiguration.loadConfiguration(customTrackConfigFile);
        customTrackConfig.set("track.name", track.getName());
        customTrackConfig.set("track.slug", track.getSlug());
        customTrackConfig.set("track.nodes", track.getNodes());
        customTrackConfig.save(customTrackConfigFile);
    }

    // Function to get the configuration for a specific track
    public FileConfiguration getTrackConfig(String trackName) {
        File trackFile = new File(tracksFolder, trackName + ".yml");
        if (trackFile.exists()) {
            return YamlConfiguration.loadConfiguration(trackFile);
        }
        return null;
    }
}
