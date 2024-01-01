package com.insurgencedev.potioneffectsaddon.listeners;

import com.insurgencedev.potioneffectsaddon.model.Effect;
import com.insurgencedev.potioneffectsaddon.model.EffectManager;
import com.insurgencedev.potioneffectsaddon.utils.CacheUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.events.IBoosterEndEvent;
import org.insurgencedev.insurgenceboosters.events.IBoosterStartEvent;
import org.insurgencedev.insurgenceboosters.libs.fo.Common;
import org.insurgencedev.insurgenceboosters.libs.fo.remain.CompMaterial;
import org.insurgencedev.insurgenceboosters.models.booster.GlobalBoosterManager;

public final class BoosterListener implements Listener {

    private final EffectManager manager;

    public BoosterListener(EffectManager manager) {
        this.manager = manager;
    }

    @EventHandler
    private void onStart(IBoosterStartEvent event) {
        String type = event.getBoosterData().getType();

        if (manager.exist(type) && event.getBoosterData().getNamespace().equals(EffectManager.BOOSTER_NAMESPACE)) {
            Effect effect = manager.getEffect(type);
            Player player = event.getPlayer();

            if (effect.getDisabledWorlds().contains(player.getWorld().getName())) {
                event.getBoosterData().setActive(false);
                return;
            }

            if (effect.isOnPlayer(player)) {
                event.getBoosterData().setActive(false);
                return;
            }

            effect.setAmplifier((int) event.getBoosterData().getMultiplier());
            Common.runLater(1, () -> effect.applyTo(player));
        }
    }

    @EventHandler
    private void onEnd(IBoosterEndEvent event) {
        String type = event.getBoosterData().getType();
        if (manager.exist(type) && event.getBoosterData().getNamespace().equals(EffectManager.BOOSTER_NAMESPACE)) {
            manager.getEffect(type).removeFrom(event.getPlayer());
        }
    }

    @EventHandler
    private void onConsume(PlayerItemConsumeEvent event) {
        if (CompMaterial.fromItem(event.getItem()).equals(CompMaterial.MILK_BUCKET)) {
            CacheUtil.actIfBoostersFound(event.getPlayer(), "apply");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onRespawn(PlayerRespawnEvent event) {
        CacheUtil.actIfBoostersFound(event.getPlayer(), "apply");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        manager.getEffectCache().values().forEach(effect -> {
            GlobalBoosterManager.BoosterFindResult gResult = IBoosterAPI.getGlobalBoosterManager().findBooster(effect.getType(), EffectManager.BOOSTER_NAMESPACE);

            if (gResult instanceof GlobalBoosterManager.BoosterFindResult.Success && !effect.getActiveList().contains(player.getUniqueId())) {
                Common.runLater(1, () -> effect.applyTo(player));
                return;
            }

            if (gResult instanceof GlobalBoosterManager.BoosterFindResult.NotFound && effect.getActiveList().contains(player.getUniqueId())) {
                Common.runLater(1, () -> effect.removeFrom(player));
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onLeave(PlayerQuitEvent event) {
        CacheUtil.actIfBoostersFound(event.getPlayer(), "remove");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onKick(PlayerKickEvent event) {
        CacheUtil.actIfBoostersFound(event.getPlayer(), "remove");
    }
}