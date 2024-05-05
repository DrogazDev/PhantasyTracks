package nl.drogaz.phantasytracks.libraries;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.changeme.nbtapi.NBT;
import net.kyori.adventure.text.Component;
import nl.drogaz.phantasytracks.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemStackBuilder {

    private ItemStack item;

    public ItemStackBuilder(Material material) {
        item = new ItemStack(material);
    }

    public ItemStackBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemStackBuilder addGlow() {
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    public ItemStackBuilder setSkullSkin(String base) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        mutateItemMeta(meta, base);
        item.setItemMeta(meta);
        return this;
    }

    private static Method metaSetProfileMethod;
    private static Field metaProfileField;

    private static GameProfile makeProfile(String b64) {
        // random uuid based on the b64 string
        UUID id = new UUID(
                b64.substring(b64.length() - 20).hashCode(),
                b64.substring(b64.length() - 10).hashCode()
        );
        GameProfile profile = new GameProfile(id, "Player");
        profile.getProperties().put("textures", new Property("textures", b64));
        return profile;
    }

    private static void mutateItemMeta(SkullMeta meta, String b64) {
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                metaSetProfileMethod.setAccessible(true);
            }
            metaSetProfileMethod.invoke(meta, makeProfile(b64));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            // if in an older API where there is no setProfile method,
            // we set the profile field directly.
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                }
                metaProfileField.set(meta, makeProfile(b64));

            } catch (NoSuchFieldException | IllegalAccessException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    public ItemStackBuilder setColor(int r, int g, int b) {
        if (!(item.getItemMeta() instanceof LeatherArmorMeta)) return this;
        LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
        meta.setColor(Color.fromRGB(r,g,b));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder setName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder setModelData(int data) {
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(data);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder addLore(String line) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        if (meta.lore() != null) {
            lore = meta.lore();
        }
        lore.add(Component.text(line));
        meta.lore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder addLores(List<String> lines) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        if (meta.lore() != null) {
            lore = meta.lore();
        }
        for (String line : lines) {
            lore.add(Component.text(line));
        }
        meta.lore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder addNbt(String nbt) {
        NBT.modify(item, nbt2 -> {
            nbt2.setString("phantasyland", nbt);
        });
        return this;
    }

    public ItemStack build() {
        return item;
    }

    public ItemStackBuilder setSkullOwner(UUID uid) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uid));
        item.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder setCustomString(String key, String value) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return this;
    }

}