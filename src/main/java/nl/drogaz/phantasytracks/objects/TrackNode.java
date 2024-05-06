package nl.drogaz.phantasytracks.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Setter
@Getter
public class TrackNode {

    private double x;
    private double y;
    private double z;
    private double rotation = 0;

    public TrackNode(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
