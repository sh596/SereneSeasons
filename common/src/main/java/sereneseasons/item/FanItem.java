package sereneseasons.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.season.BossBarHandler;

public class FanItem extends Item {
    private static final Logger log = LoggerFactory.getLogger(FanItem.class);

    public FanItem(Properties properties) {
        super(properties);
    }

    private static int counting = 0;

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean isSelected) {
        // 서버 측에서만 실행
        if (!level.isClientSide && entity instanceof Player player) {
            // Mainhand와 Offhand 중 하나에 있는지 확인
            boolean isInMainHand = player.getMainHandItem() == stack;
            boolean isInOffHand = player.getOffhandItem() == stack;

            if ((isInMainHand || isInOffHand) && counting == 0) {
                adjustWorldTemperature(level, -12f);
                counting++;
            }
            else if(!isInMainHand && !isInOffHand && counting == 1) {
                adjustWorldTemperature(level, 12f);
                counting--;
            }
        }

        super.inventoryTick(stack, level, entity, slot, isSelected);
    }

    private void adjustWorldTemperature(Level level, float delta) {
        if (level instanceof ServerLevel serverLevel) {
            BossBarHandler.worldTemp += delta; // BossBarHandler의 worldTemp 조정
            log.info("FanItem: Adjusted world temperature by {}, new temperature: {}", delta, BossBarHandler.worldTemp);
        }
    }
}
