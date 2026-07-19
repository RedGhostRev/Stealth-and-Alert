package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class EyeContactCondition implements IAlertCondition {
    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        // 物理检查：南瓜头遮蔽判定
        boolean checkPumpkin = getBool(params, "pumpkin_mask", false);
        if (checkPumpkin) {
            // 检查头盔栏 (slot 3) 是否为南瓜头
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(Items.CARVED_PUMPKIN)) {
                return false; // 戴了南瓜头，对视断言直接失效
            }
        }

        // 灵敏度参数
        double sensitivity = 0.025;

        // 几何运算
        Vec3 playerLookVec = player.getViewVector(1.0F).normalize();
        Vec3 toMobVec = new Vec3(mob.getX() - player.getX(), mob.getEyeY() - player.getEyeY(), mob.getZ() - player.getZ());
        double distance = toMobVec.length();

        if (distance < 0.001) return true; // 极近距离视为对视

        toMobVec = toMobVec.normalize();
        double dotProduct = playerLookVec.dot(toMobVec);

        return dotProduct > 1.0 - sensitivity / distance;
    }
}
