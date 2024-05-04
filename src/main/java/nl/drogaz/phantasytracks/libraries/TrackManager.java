package nl.drogaz.phantasytracks.libraries;

import lombok.Getter;
import nl.drogaz.phantasytracks.Main;
import nl.drogaz.phantasytracks.objects.Track;
import nl.drogaz.phantasytracks.objects.TrackNode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TrackManager {
    public List<Track> getAllTracks() {

            File tracksFolder = new File(Main.getInstance().getDataFolder(), "tracks");
            File[] trackFiles = tracksFolder.listFiles();

            List<Track> tracks = new ArrayList<>();

            for (File trackFile : trackFiles) {
                FileConfiguration trackConfig = YamlConfiguration.loadConfiguration(trackFile);
                List<TrackNode> nodes = (List<TrackNode>) trackConfig.getList("track.nodes");
                Track track = new Track(trackConfig.getString("track.name"), trackConfig.getString("track.slug"), nodes);
                tracks.add(track);
            }

            return tracks;

    }

}
