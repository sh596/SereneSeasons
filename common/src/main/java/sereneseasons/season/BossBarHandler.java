package sereneseasons.season;

import glitchcore.event.TickEvent;
import glitchcore.event.player.PlayerEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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


public class BossBarHandler {
    private static final Logger log = LoggerFactory.getLogger(BossBarHandler.class);

    public static float progress = 0.5f;
    private static int tickcount;

    public static void onJoinLevel(PlayerEvent.JoinLevel event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            BossBar.showBossBar(serverPlayer);
        }
    }

    public static float worldTemp = 20.0f;
    static float playerTemp = 36.5f;
    public static void onWorldTick(TickEvent.Level event) {

        PlayerTemperatureSavedData savedData = getPlayerTemperatureSavedData(event.getLevel());
        SeasonSavedData seasonSavedData = getSeasonSavedData(event.getLevel());

        savedData.playerTemperature = playerTemp;

        tickcount++;
        if(seasonSavedData.seasonCycleTicks % SeasonTime.ZERO.getDayDuration() == 2){
            worldTemp = TemperatureHandler.getTemp(event.getLevel());
        }
        if (tickcount >= Integer.MAX_VALUE) {
            tickcount = 0;
        }
        if(tickcount % 60 == 0) {
            if(worldTemp < 15) {
                if(worldTemp <= 5){
                    playerTemp -= 0.01f;
                }
                playerTemp -= 0.003f;
            }
            else if(worldTemp > 23) {
                if(worldTemp >= 30){
                    playerTemp += 0.01f;
                }
                playerTemp += 0.003f;
            }
            else{
                if(playerTemp > 36.5f){
                    playerTemp -= 0.001f;
                }
                else{
                    playerTemp += 0.001f;
                }
            }
        }
        log.info(String.valueOf(playerTemp));
        log.info("log temp : "+ (worldTemp) + " " + tickcount);


        progress = (playerTemp - 33.0f) * 0.142f;
        BossBar.updateBossBar(progress, playerTemp);
        savedData.setDirty();

        //event.getLevel().tickRateManager().setTickRate(1);
    }



    public static PlayerTemperatureSavedData getPlayerTemperatureSavedData(Level w) {
        if (w.isClientSide() || !(w instanceof ServerLevel)) {
            return null;
        }

        ServerLevel world = (ServerLevel) w;
        DimensionDataStorage saveDataManager = world.getChunkSource().getDataStorage();

        Supplier<PlayerTemperatureSavedData> defaultSaveDataSupplier = () ->
        {
            PlayerTemperatureSavedData savedData = new PlayerTemperatureSavedData();

            int startingSeason = ModConfig.seasons.startingSubSeason;

            if (startingSeason == 0) {
                savedData.playerTemperature = (world.random.nextInt(12)) * SeasonTime.ZERO.getSubSeasonDuration();
            }

            if (startingSeason > 0) {
                savedData.playerTemperature = (startingSeason - 1) * SeasonTime.ZERO.getSubSeasonDuration();
            }

            savedData.setDirty(); //Mark for saving
            return savedData;
        };

        return saveDataManager.computeIfAbsent(new SavedData.Factory<>(defaultSaveDataSupplier, PlayerTemperatureSavedData::load, DataFixTypes.LEVEL), PlayerTemperatureSavedData.DATA_IDENTIFIER);
    }
}