package sereneseasons.season;

import glitchcore.event.TickEvent;
import glitchcore.event.player.PlayerEvent;
import glitchcore.event.player.PlayerInteractEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import java.util.logging.Handler;

import static net.minecraft.world.damagesource.DamageTypes.GENERIC;

public class TemperatureHandler {


    private static final Logger log = LoggerFactory.getLogger(TemperatureHandler.class);

    public static void onLevelTick(TickEvent.Level event){
        Level level = event.getLevel();
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            log.info(serverLevel.players().toString());
            for (ServerPlayer player : serverLevel.players()) {
                player.hurt(source(DamageTypes.IN_FIRE), );
            }
        }
        Season.SubSeason subSeason = SeasonHelper.getSeasonState(event.getLevel()).getSubSeason();
        log.info(event.getLevel().players().toString());
    }
    public static void onJoinLevel(PlayerEvent.JoinLevel event){
        log.info(String.valueOf(event.getPlayer().tickCount));
    }

    public static float getTemp(Level level){
        Season.SubSeason subSeason = SeasonHelper.getSeasonState(level).getSubSeason();
        return calculateTemperature(subSeason);
    }

    private static float calculateTemperature(Season.SubSeason subSeason) {
        switch (subSeason) {
            case EARLY_SPRING:
                return 4.2F;
            case MID_SPRING:
                return 11.7F;
            case LATE_SPRING:
                return 17.0F;

            case EARLY_SUMMER:
                return 23.0F;
            case MID_SUMMER:
                return 26.0F;
            case LATE_SUMMER:
                return 26.3F;

            case EARLY_AUTUMN:
                return 22.0F;
            case MID_AUTUMN:
                return 15.0F;
            case LATE_AUTUMN:
                return 7.0F;

            case EARLY_WINTER:
                return -1.4F;
            case MID_WINTER:
                return -5.0F;
            case LATE_WINTER:
                return -2.0F;

            default:
                return 0.0F; // 기본값, 예기치 않은 값이 들어올 경우
        }
    }

    private DamageSource source(ResourceKey<DamageType> $$0) {
        return new DamageSource(this.damageTypes.getHolderOrThrow($$0));
    }
}
