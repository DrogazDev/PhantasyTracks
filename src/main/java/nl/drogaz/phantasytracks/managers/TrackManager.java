package nl.drogaz.phantasytracks.managers;

import com.google.gson.Gson;
import lombok.Getter;
import nl.drogaz.phantasytracks.Main;
import nl.drogaz.phantasytracks.objects.Track;
import nl.drogaz.phantasytracks.objects.TrackNode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class TrackManager {

    public Track getTrackByName(String name) {

            File trackFile = new File(Main.getInstance().getDataFolder(), "tracks/" + name + ".yml");
            FileConfiguration trackConfig = YamlConfiguration.loadConfiguration(trackFile);
            List<TrackNode> nodes = (List<TrackNode>) trackConfig.getList("track.nodes");
            return new Track(trackConfig.getString("track.name"), trackConfig.getString("track.slug"), nodes);

    }

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

    public void addNodeToTrack(Track track, TrackNode node) {
        List<TrackNode> nodes = track.getNodes();
        nodes.add(node);
        track.setNodes(nodes);
        saveNodesToTrack(track);
    }

    public void saveNodesToTrack(Track track) {

        Gson gson = new Gson();
        String json = gson.toJson(track.getNodes());

        File trackFile = new File(Main.getInstance().getDataFolder(), "tracks/" + track.getName() + ".yml");
        FileConfiguration trackConfig = YamlConfiguration.loadConfiguration(trackFile);
        trackConfig.set("track.nodes", json);
        try {
            trackConfig.save(trackFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TrackNode> getNodes(Track track) {
        File trackFile = new File(Main.getInstance().getDataFolder(), "tracks/" + track.getName() + ".yml");
        FileConfiguration trackConfig = YamlConfiguration.loadConfiguration(trackFile);
        String json = trackConfig.getString("track.nodes");
        Gson gson = new Gson();
        TrackNode[] nodes = gson.fromJson(json, TrackNode[].class);
        List<TrackNode> nodeList = new ArrayList<>(Arrays.asList(nodes));
        track.setNodes(nodeList);
        return nodeList;
    }
}
