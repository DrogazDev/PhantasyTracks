package nl.drogaz.phantasytracks.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public class Cart {

    private Material material;
    private Integer maxPassengers;
    private double seatOffset;



}
