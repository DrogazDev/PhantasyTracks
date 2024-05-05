package nl.drogaz.phantasytracks.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Track {

    private String name;
    private String slug;
    private List<TrackNode> nodes;

    public Track getTrackByName(String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }
}
