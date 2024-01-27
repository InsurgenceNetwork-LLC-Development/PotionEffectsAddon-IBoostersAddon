package com.insurgencedev.potioneffectsaddon.utils;

import com.insurgencedev.potioneffectsaddon.PotionEffectsAddon;
import com.insurgencedev.potioneffectsaddon.model.Effect;
import com.insurgencedev.potioneffectsaddon.model.EffectManager;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.data.BoosterFindResult;
import org.insurgencedev.insurgenceboosters.libs.fo.Common;

@UtilityClass
public class CacheUtil {

    public void actIfBoostersFound(Player player, String action) {
        PotionEffectsAddon.instance().getManager().getEffectCache().values().forEach(effect -> {
            BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(player).getBoosterDataManager()
                    .findActiveBooster(effect.getType(), EffectManager.BOOSTER_NAMESPACE);

            if (pResult instanceof BoosterFindResult.Success) {
                performAction(effect, player, action);
                return;
            }

            IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(effect.getType(), EffectManager.BOOSTER_NAMESPACE, globalBooster -> {
                performAction(effect, player, action);
                return null;
            }, () -> null);
        });
    }

    private void performAction(Effect effect, Player player, String action) {
        switch (action) {
            case "apply" -> Common.runLater(1, () -> effect.applyTo(player));
            case "remove" -> Common.runLater(1, () -> effect.removeFrom(player));
        }
    }

}