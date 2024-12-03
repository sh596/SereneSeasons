package sereneseasons.season;

import glitchcore.event.player.PlayerEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import sereneseasons.init.ModConfig;
import sereneseasons.init.ModPackets;
import sereneseasons.network.SyncSeasonCyclePacket;

import java.util.HashMap;
import java.util.function.Supplier;

import static sereneseasons.season.SeasonHandler.*;

public class PlayerTemperatureHandler {
    public static final HashMap<Level, Integer> updateTicks = new HashMap<>();



    public static void onJoinLevel(PlayerEvent.JoinLevel event)
    {

        if (!(event.getPlayer() instanceof ServerPlayer player))
            return;
        Level level = player.level();

        TemperatureSavedData savedData = getTemperatureSavedData(level);
//        savedData.playerTemperature = Mth.positiveModulo(savedData.playerTemperature + (int), SeasonTime.ZERO.getCycleDuration());

        int ticks = updateTicks.getOrDefault(level, 0);
        if (ticks >= 20)
        {
            ticks %= 20;
        }
        updateTicks.put(level, ticks + 1);
        savedData.setDirty();
    }

    public static SeasonSavedData getSeasonSavedData(Level w)
    {
        if (w.isClientSide() || !(w instanceof ServerLevel))
        {
            return null;
        }

        ServerLevel world = (ServerLevel)w;
        DimensionDataStorage saveDataManager = world.getChunkSource().getDataStorage();

        Supplier<SeasonSavedData> defaultSaveDataSupplier = () ->
        {
            SeasonSavedData savedData = new SeasonSavedData();

            int startingSeason = ModConfig.seasons.startingSubSeason;

            if (startingSeason == 0)
            {
                savedData.seasonCycleTicks = (world.random.nextInt(12)) * SeasonTime.ZERO.getSubSeasonDuration();
            }

            if (startingSeason > 0)
            {
                savedData.seasonCycleTicks = (startingSeason - 1) * SeasonTime.ZERO.getSubSeasonDuration();
            }

            savedData.setDirty(); //Mark for saving
            return savedData;
        };

        return saveDataManager.computeIfAbsent(new SavedData.Factory<>(defaultSaveDataSupplier, SeasonSavedData::load, DataFixTypes.LEVEL), SeasonSavedData.DATA_IDENTIFIER);
    }
    public static TemperatureSavedData getTemperatureSavedData(Level w)
    {
        if (w.isClientSide() || !(w instanceof ServerLevel))
        {
            return null;
        }

        ServerLevel world = (ServerLevel)w;
        DimensionDataStorage saveDataManager = world.getChunkSource().getDataStorage();

        Supplier<TemperatureSavedData> defaultSaveDataSupplier = () ->
        {
            TemperatureSavedData savedData = new TemperatureSavedData();

            int startingSeason = ModConfig.seasons.startingSubSeason;

            if (startingSeason == 0)
            {
                savedData.playerTemperature = (world.random.nextInt(12)) * SeasonTime.ZERO.getSubSeasonDuration();
            }

            if (startingSeason > 0)
            {
                savedData.playerTemperature = (startingSeason - 1) * SeasonTime.ZERO.getSubSeasonDuration();
            }

            savedData.setDirty(); //Mark for saving
            return savedData;
        };

        return saveDataManager.computeIfAbsent(new SavedData.Factory<>(defaultSaveDataSupplier, TemperatureSavedData::load, DataFixTypes.LEVEL), SeasonSavedData.DATA_IDENTIFIER);
    }
}
