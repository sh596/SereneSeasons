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

public class WarmChestplateItem extends ArmorItem {
    private static final Logger log = LoggerFactory.getLogger(WarmChestplateItem.class);
    private static final HashMap<UUID, Boolean> playerChestplateStatus = new HashMap<>();

    public WarmChestplateItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            UUID playerUUID = player.getUUID();
            ItemStack chestplateStack = player.getItemBySlot(EquipmentSlot.CHEST);

            boolean isWearingChestplate = chestplateStack.getItem() == this;
            boolean wasWearingChestplate = playerChestplateStatus.getOrDefault(playerUUID, false);

            if (isWearingChestplate != wasWearingChestplate) {
                if (isWearingChestplate) {
                    adjustWorldTemperature(level, 6.0f); // 착용 시
                } else {
                    adjustWorldTemperature(level, -6.0f); // 해제 시
                }
                playerChestplateStatus.put(playerUUID, isWearingChestplate);
                log.info("Player {} chestplate status changed: {} -> {}", player.getName().getString(), wasWearingChestplate, isWearingChestplate);
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
