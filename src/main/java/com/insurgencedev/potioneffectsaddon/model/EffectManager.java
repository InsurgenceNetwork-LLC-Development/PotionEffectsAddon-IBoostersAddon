package com.insurgencedev.potioneffectsaddon.model;

import lombok.Getter;
import org.insurgencedev.insurgenceboosters.api.addon.AddonConfig;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class EffectManager extends AddonConfig {

    public static final String BOOSTER_NAMESPACE = "CUSTOM_EFFECT";
    private final Map<String, Effect> effectCache;

    public EffectManager() {
        effectCache = new HashMap<>();
        loadAddonConfig("config.yml", "effects.yml");
    }

    @Override
    protected void onLoad() {
        effectCache.clear();
        getList("Effects", Effect.class).forEach(this::addToCache);
    }

    private void addToCache(Effect effect) {
        effectCache.put(effect.getType(), effect);
    }

    public boolean exist(String type) {
        return effectCache.containsKey(type);
    }

    public Effect getEffect(String type) {
        return effectCache.get(type);
    }
}
