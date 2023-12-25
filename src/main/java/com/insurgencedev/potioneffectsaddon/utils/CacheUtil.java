package com.insurgencedev.potioneffectsaddon.utils;

import com.insurgencedev.potioneffectsaddon.PotionEffectsAddon;
import com.insurgencedev.potioneffectsaddon.model.Effect;
import com.insurgencedev.potioneffectsaddon.model.EffectManager;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.libs.fo.Common;
import org.insurgencedev.insurgenceboosters.models.booster.GlobalBoosterManager;
import org.insurgencedev.insurgenceboosters.settings.IBoostersPlayerCache;

@UtilityClass
public class CacheUtil {

    public void actIfBoostersFound(Player player, String action) {
        PotionEffectsAddon.instance().getManager().getEffectCache().values().forEach(effect -> {
            GlobalBoosterManager.BoosterFindResult gResult = IBoosterAPI.getGlobalBoosterManager().findBooster(effect.getType(), EffectManager.BOOSTER_NAMESPACE);
            if (gResult instanceof GlobalBoosterManager.BoosterFindResult.Success) {
                performAction(effect, player, action);
                return;
            }

            IBoostersPlayerCache.BoosterFindResult pResult = IBoosterAPI.getCache(player).findActiveBooster(effect.getType(), EffectManager.BOOSTER_NAMESPACE);
            if (pResult instanceof IBoostersPlayerCache.BoosterFindResult.Success) {
                performAction(effect, player, action);
            }
        });
    }

    private void performAction(Effect effect, Player player, String action) {
        switch (action) {
            case "apply" -> Common.runLater(1, () -> effect.applyTo(player));
            case "remove" -> Common.runLater(1, () -> effect.removeFrom(player));
        }
    }

}