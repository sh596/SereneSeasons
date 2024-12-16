package sereneseasons.season;

import glitchcore.event.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.core.SereneSeasons;
import sereneseasons.effect.ColdEffect;
import sereneseasons.effect.SSEffects;
import sereneseasons.init.ModConfig;

import java.util.function.Supplier;

public class TemperatureHandler {

    private static final Logger log = LoggerFactory.getLogger(TemperatureHandler.class);
    private static final float SNOW_CHANCE = 0.2f;

    static int tickCounter = 0;
    private static int snowCounter = 0;
    private static boolean isSnow = false;

    public static void onLevelTick(TickEvent.Level event) {
        tickCounter++;

        if (tickCounter % 2 == 0) {
            processSeason(event);
        }
        log.info("Damage");
        resetTickCounterIfNeeded();
        for (Player player:event.getLevel().players()){
            if(player != null){

//                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10000, 1));
            }
        }
    }

    private static void processSeason(TickEvent.Level event) {
        Season.SubSeason subSeason = SeasonHelper.getSeasonState(event.getLevel()).getSubSeason();
        SeasonSavedData seasonData = getSeasonSavedData(event.getLevel());
        TemperatureSavedData temperatureData = getTemperatureSavedData(event.getLevel());

        if (subSeason.equals(Season.SubSeason.EARLY_SPRING)) {
            spawnSandParticlesForPlayers(event.getLevel());
        }

        if (isSnow) {
            handleSnowEffects(event.getLevel());
        }

        if (seasonData.seasonCycleTicks != 0) {
            updateSavedData(event.getLevel(), temperatureData, seasonData.seasonCycleTicks, subSeason);
        }

        log.info("Season cycle ticks: {}", seasonData.seasonCycleTicks);
    }

    private static void resetTickCounterIfNeeded() {
        if (tickCounter >= Integer.MAX_VALUE) {
            tickCounter = 0;
        }
    }

    public static float getTemp(Level level) {
        TemperatureSavedData temperatureData = getTemperatureSavedData(level);
        assert temperatureData != null;
        return temperatureData.temperature;
    }

    private static void updateSavedData(Level level, TemperatureSavedData temperatureData, int ticks, Season.SubSeason subSeason) {
        if (ticks % SeasonTime.ZERO.getDayDuration() == 1) {
            isSnow = false;
            adjustTemperature(level, temperatureData, subSeason);
        }
    }

    private static void adjustTemperature(Level level, TemperatureSavedData temperatureData, Season.SubSeason subSeason) {
        float newTemperature;

        switch (subSeason) {
            case EARLY_SPRING -> newTemperature = getRandomTemperature(level, -2.0f, 11.0f);
            case MID_SPRING -> newTemperature = getRandomTemperature(level, 4.0f, 18.0f);
            case LATE_SPRING -> newTemperature = getRandomTemperature(level, 10.0f, 24.0f);
            case EARLY_SUMMER -> newTemperature = getRandomTemperature(level, 17.0f, 27.0f);
            case MID_SUMMER -> newTemperature = getRandomTemperature(level, 21.0f, 29.0f);
            case LATE_SUMMER -> newTemperature = handleLateSummer(level, temperatureData);
            case EARLY_AUTUMN -> newTemperature = getRandomTemperature(level, 15.0f, 26.0f);
            case MID_AUTUMN -> newTemperature = getRandomTemperature(level, 6.0f, 20.0f);
            case LATE_AUTUMN -> newTemperature = getRandomTemperature(level, -1.0f, 11.0f);
            case EARLY_WINTER -> newTemperature = getRandomTemperature(level, -9.0f, 3.0f);
            case MID_WINTER -> {
                newTemperature = getRandomTemperature(level, -12.0f, 2.0f);
                if (level.random.nextInt(8) == 0) {
                    isSnow = true;
                }
            }
            case LATE_WINTER -> newTemperature = getRandomTemperature(level, -8.0f, 5.0f);
            default -> newTemperature = 0.0f;
        }

        temperatureData.temperature = newTemperature;
        temperatureData.setDirty();
    }

    private static float handleLateSummer(Level level, TemperatureSavedData temperatureData) {
        if (level.random.nextInt(3) == 0) {
            notifyHeatWave(level);
            return 35.0f;
        } else {
            return getRandomTemperature(level, 21.0f, 30.0f);
        }
    }

    private static void notifyHeatWave(Level level) {
        spawnFlameParticlesForPlayers(level);
        Component message = Component.translatable("폭염!").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000)));
        level.players().forEach(player -> player.sendSystemMessage(message));
    }

    private static float getRandomTemperature(Level level, float min, float max) {
        return min + (max - min) * level.random.nextFloat();
    }

    static TemperatureSavedData getTemperatureSavedData(Level level) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return null;
        }

        return serverLevel.getChunkSource().getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(
                        TemperatureSavedData::new,
                        TemperatureSavedData::load,
                        DataFixTypes.LEVEL
                ), TemperatureSavedData.DATA_IDENTIFIER);
    }

    private static SeasonSavedData getSeasonSavedData(Level level) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return null;
        }

        return serverLevel.getChunkSource().getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(
                        SeasonSavedData::new,
                        SeasonSavedData::load,
                        DataFixTypes.LEVEL
                ), SeasonSavedData.DATA_IDENTIFIER);
    }

    private static void spawnSandParticlesForPlayers(Level level) {
        level.players().stream()
                .filter(player -> player.position().y > 65)
                .forEach(player -> spawnSandParticle((ServerLevel) level, player.position()));
    }

    private static void handleSnowEffects(Level level) {
        snowCounter++;
        level.players().stream()
                .filter(player -> player.position().y > 65)
                .forEach(player -> {
                    addSnowInRadius((ServerLevel) level, 15);
                    spawnSnowParticle((ServerLevel) level, player.position());
                });

        if (snowCounter == 20000) {
            snowCounter = 0;
            isSnow = false;
        }
    }

    private static void spawnSandParticle(ServerLevel level, Vec3 position) {
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState());
        level.sendParticles(particle, position.x + 0.5, position.y + 0.5, position.z + 0.5, 100, 16.0, 16.0, 16.0, 1);
    }

    private static void spawnSnowParticle(ServerLevel level, Vec3 position) {
        level.sendParticles(ParticleTypes.SNOWFLAKE, position.x + 0.5, position.y + 0.5, position.z + 0.5, 100, 10.0, 10.0, 10.0, 10);
    }

    private static void spawnFlameParticlesForPlayers(Level level) {
        level.players().forEach(player -> spawnFlameParticle((ServerLevel) level, player.position()));
    }

    private static void spawnFlameParticle(ServerLevel level, Vec3 position) {
        level.sendParticles(ParticleTypes.FLAME, position.x + 0.5, position.y + 0.5, position.z + 0.5, 50, 1.0, 1.0, 1.0, 1);
    }

    private static void addSnowInRadius(ServerLevel level, int radius) {
        BlockPos playerPos = level.getRandomPlayer().blockPosition();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos pos = playerPos.offset(x, 0, z);
                BlockPos topPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);

                if (level.getRandom().nextFloat() < SNOW_CHANCE) {
                    addSnowLayer(level, topPos);
                }
            }
        }
    }

    private static void addSnowLayer(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.WATER)) {
            return;
        }

        if (state.getBlock() == Blocks.SNOW) {
            int layers = state.getValue(SnowLayerBlock.LAYERS);
            if (layers < 8) {
                level.setBlockAndUpdate(pos, state.setValue(SnowLayerBlock.LAYERS, layers + 1));
            }
        } else if (state.isAir()) {
            level.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 1));
        }
    }
}
