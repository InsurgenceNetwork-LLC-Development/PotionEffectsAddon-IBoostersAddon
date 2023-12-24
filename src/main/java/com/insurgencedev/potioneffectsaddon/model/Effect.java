package com.insurgencedev.potioneffectsaddon.model;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.insurgencedev.insurgenceboosters.libs.fo.collection.SerializedMap;
import org.insurgencedev.insurgenceboosters.libs.fo.model.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Effect implements ConfigSerializable {

    private final int amplifier;
    private final String type;
    private final String namespace;
    private final List<UUID> activeList;
    private final List<String> disabledWorlds;

    public Effect(int amplifier, String type, String namespace, List<String> disabledWorlds) {
        this.amplifier = amplifier;
        this.type = type;
        this.namespace = namespace;
        this.activeList = new ArrayList<>();
        this.disabledWorlds = disabledWorlds;
    }

    private void addToActiveList(Player player) {
        if (!activeList.contains(player.getUniqueId())) {
            activeList.add(player.getUniqueId());
        }
    }

    private void removeFromActiveList(Player player) {
        activeList.remove(player.getUniqueId());
    }

    public boolean isOnPlayer(Player player) {
        PotionEffectType type = PotionEffectType.getByName(namespace);
        return type != null && player.hasPotionEffect(type);
    }

    public void apply(Player player) {
        PotionEffectType type = PotionEffectType.getByName(namespace);
        if (type != null) {
            player.addPotionEffect(type.createEffect(Integer.MAX_VALUE, amplifier - 1));
            addToActiveList(player);
        }
    }

    public void remove(Player player) {
        PotionEffectType type = PotionEffectType.getByName(namespace);
        if (type != null) {
            player.removePotionEffect(type);
            removeFromActiveList(player);
        }
    }

    public static Effect deserialize(SerializedMap map) {
        return new Effect(
                map.getInteger("Amplifier"),
                map.getString("Type"),
                map.getString("Effect").toUpperCase(),
                map.getStringList("Disabled_Worlds")
        );
    }

    @Override
    public SerializedMap serialize() {
        return null;
    }
}
