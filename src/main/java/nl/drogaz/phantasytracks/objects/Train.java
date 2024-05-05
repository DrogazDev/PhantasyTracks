package nl.drogaz.phantasytracks.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Train {

    private List<Cart> carts;
    private double distance;
    private double bps;
    private Track track;
    private double trackOffsetY;

}
