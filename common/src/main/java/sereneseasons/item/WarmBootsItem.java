package sereneseasons.item;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.init.ModConfig;
import sereneseasons.season.*;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

public class WarmBootsItem extends ArmorItem {
    private static final Logger log = LoggerFactory.getLogger(WarmBootsItem.class);
    private static final HashMap<UUID, Boolean> playerBootsStatus = new HashMap<>();

    public WarmBootsItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            UUID playerUUID = player.getUUID();
            ItemStack bootsStack = player.getItemBySlot(EquipmentSlot.FEET);

            boolean isWearingBoots = bootsStack.getItem() == this;
            boolean wasWearingBoots = playerBootsStatus.getOrDefault(playerUUID, false);

            if (isWearingBoots != wasWearingBoots) {
                if (isWearingBoots) {
                    adjustWorldTemperature(level, 3.0f); // 착용 시
                } else {
                    adjustWorldTemperature(level, -3.0f); // 해제 시
                }
                playerBootsStatus.put(playerUUID, isWearingBoots);
                log.info("Player {} boots status changed: {} -> {}", player.getName().getString(), wasWearingBoots, isWearingBoots);
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
