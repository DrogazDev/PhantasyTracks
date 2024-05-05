package nl.drogaz.phantasytracks.libraries;

import lombok.Getter;
import nl.drogaz.phantasytracks.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@Getter
public class PersistentTags {
    public static boolean checkData(String key, String value, ItemMeta meta) {
        if (meta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING)) {
            return meta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING).equals(value);
        } else {
            return false;
        }
    }

}
