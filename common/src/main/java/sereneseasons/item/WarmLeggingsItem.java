package sereneseasons.item;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.season.BossBarHandler;
import sereneseasons.season.TemperatureHandler;
import sereneseasons.season.TemperatureSavedData;

import java.util.HashMap;
import java.util.UUID;

public class WarmLeggingsItem extends ArmorItem {
    private static final Logger log = LoggerFactory.getLogger(WarmLeggingsItem.class);

    // 플레이어별 헬멧 착용 상태를 관리
    private static final HashMap<UUID, Boolean> playerLeggingsStatus = new HashMap<>();

    public WarmLeggingsItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            UUID playerUUID = player.getUUID();
            ItemStack leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);

            // 현재 레깅스가 WarmLeggingsItem인지 확인
            boolean isWearingLeggings = leggingsStack.getItem() == this;
            boolean wasWearingLeggings = playerLeggingsStatus.getOrDefault(playerUUID, false);

            // 착용 상태가 변경되었을 때만 처리
            if (isWearingLeggings != wasWearingLeggings) {
                if (isWearingLeggings) {
                    adjustWorldTemperature(level, 5.0f); // 착용했을 때 온도 상승
                } else {
                    adjustWorldTemperature(level, -5.0f); // 벗었을 때 온도 하강
                }
                playerLeggingsStatus.put(playerUUID, isWearingLeggings); // 상태 갱신
                log.info("Player {} helmet status changed: {} -> {}", player.getName().getString(), wasWearingLeggings, isWearingLeggings);
            }
        }
        super.inventoryTick(stack, level, entity, slot, isSelected);
    }

    private void adjustWorldTemperature(Level level, float delta) {
        if (level instanceof ServerLevel serverLevel) {
            BossBarHandler.worldTemp += delta;
        }
    }
}