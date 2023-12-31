package com.insurgencedev.potioneffectsaddon;

import com.insurgencedev.potioneffectsaddon.listeners.BoosterListener;
import com.insurgencedev.potioneffectsaddon.model.EffectManager;
import lombok.Getter;
import org.insurgencedev.insurgenceboosters.api.addon.IBoostersAddon;
import org.insurgencedev.insurgenceboosters.api.addon.InsurgenceBoostersAddon;

@Getter
@IBoostersAddon(name = "PotionEffects", version = "1.0.1", author = "Insurgence Dev Team", description = "Apply Potion Effects")
public class PotionEffectsAddon extends InsurgenceBoostersAddon {

    private static PotionEffectsAddon instance;
    private EffectManager manager;

    @Override
    public void onAddonStart() {
        instance = this;
    }

    @Override
    public void onAddonReloadablesStart() {
        manager = new EffectManager();
        registerEvent(new BoosterListener(manager));
    }

    public static PotionEffectsAddon instance() {
        return instance;
    }

}
