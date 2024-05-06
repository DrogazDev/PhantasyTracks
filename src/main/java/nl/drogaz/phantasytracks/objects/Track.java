package nl.drogaz.phantasytracks.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Track {

    private String name;
    private String slug;
    private List<TrackNode> nodes;
}
