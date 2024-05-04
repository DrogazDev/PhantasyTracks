package nl.drogaz.phantasytracks.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TrackNode {

    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;

    public void setCoords(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setRotation(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double[] getCoords() {
        return new double[] {x, y, z};
    }

    public double[] getRotation() {
        return new double[] {yaw, pitch};
    }

    public void setCoords(double[] coords) {
        this.x = coords[0];
        this.y = coords[1];
        this.z = coords[2];
    }

    public void setRotation(double[] rotation) {
        this.yaw = rotation[0];
        this.pitch = rotation[1];
    }
}
