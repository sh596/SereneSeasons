package sereneseasons.season;

import glitchcore.event.TickEvent;
import glitchcore.event.player.PlayerEvent;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.bossbar.BossBar;
import sereneseasons.init.ModConfig;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.util.datafix.DataFixTypes;
import sereneseasons.init.ModConfig;
import java.util.HashMap;
import java.util.function.Supplier;

import java.util.function.Supplier;

import static sereneseasons.season.SeasonHandler.getSeasonSavedData;
import static sereneseasons.season.TemperatureHandler.getTemperatureSavedData;
import static sereneseasons.season.TemperatureHandler.tickCounter;


public class BossBarHandler {
    private static final Logger log = LoggerFactory.getLogger(BossBarHandler.class);

    public static float progress = 0.5f;
    private static int tickcount;

    public static void onJoinLevel(PlayerEvent.JoinLevel event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            BossBar.showBossBar(serverPlayer);

        }
    }

    static float worldTemp = 0.0f;
    public static void onWorldTick(TickEvent.Level event) {

        TemperatureSavedData worldTemperatureData = getTemperatureSavedData(event.getLevel());
        PlayerTemperatureSavedData savedData = getPlayerTemperatureSavedData(event.getLevel());
        SeasonSavedData seasonSavedData = getSeasonSavedData(event.getLevel());
        tickcount++;
        if(seasonSavedData.seasonCycleTicks % SeasonTime.ZERO.getDayDuration() == 3){
            worldTemp = TemperatureHandler.getTemp(event.getLevel());
            log.info("world temp 1" + TemperatureHandler.getTemp(event.getLevel()));
        }

//        log.info("world temp 2" + TemperatureHandler.getTemp(event.getLevel()) + " "+ tickcount);


        if (tickcount >= Integer.MAX_VALUE) {
            tickcount = 0;
        }
        if(tickcount % 20 == 0) {
            if(savedData.playerTemperature > worldTemp){
                savedData.playerTemperature -= 0.1f;
                savedData.setDirty();
            }
            if(savedData.playerTemperature < worldTemp){
                savedData.playerTemperature += 0.1f;
                savedData.setDirty();
            }
        }
        log.info(String.valueOf(savedData.playerTemperature));
        log.info("log temp : "+ (worldTemp) + " " + tickcount);


        progress = savedData.playerTemperature * 0.013f;
        BossBar.updateBossBar(progress, savedData.playerTemperature);
        savedData.setDirty();

        //event.getLevel().tickRateManager().setTickRate(1);
    }



    public static PlayerTemperatureSavedData getPlayerTemperatureSavedData(Level w) {
        if (w.isClientSide() || !(w instanceof ServerLevel serverLevel)) {
            return null;
        }

        return serverLevel.getChunkSource().getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(
                        PlayerTemperatureSavedData::new,
                        PlayerTemperatureSavedData::load,
                        DataFixTypes.LEVEL
                ), PlayerTemperatureSavedData.DATA_IDENTIFIER);
    }
}