package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.compat.CompatHandler;
import net.rev.stealthandalert.compat.ironsspellbooks.IronsSpellbooksCompat;

import java.util.Map;
import java.util.UUID;

public class FightForOwnerCondition implements IAlertCondition {
    public static final String ID = StealthAndAlert.MOD_ID + ":fight_for_owner";

    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        Entity owner = null;
        if (CompatHandler.HAS_IRONS_SPELLBOOKS) {
            owner = IronsSpellbooksCompat.getOwner(mob);
        }
        if (mob instanceof OwnableEntity ownable) {
            owner = owner == null ? ownable.getOwner() : owner;
        }
        if (owner == null) return false;
        if (owner instanceof Player pOwner) {
            if (pOwner.isAlliedTo(player)) return false;
        }
        AlertData alertData = owner.getData(ModAttachments.ALERT_DATA);
        boolean hasMemory = alertData.targetMemoryTicks().getOrDefault(player.getUUID(), 0) > 0;
        Map<UUID, Long> map = owner.getData(ModAttachments.EVENT_LISTENER_DATA).eventStates().get(ID);
        Long lastHurt = map.getOrDefault(player.getUUID(), -1L);
        boolean rememberHurt = lastHurt > -1 && mob.level().getGameTime() - lastHurt < 200L;
        boolean isTarget = false;
        if (owner instanceof Mob) isTarget = ((Mob) owner).getTarget() == player;
        return hasMemory || rememberHurt || isTarget;
    }
}
