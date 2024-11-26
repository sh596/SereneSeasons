package sereneseasons.season;

import glitchcore.event.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.init.ModConfig;

import java.awt.*;
import java.util.function.Supplier;

public class TemperatureHandler {

    private static int tickCounter = 0;
    private static final Logger log = LoggerFactory.getLogger(TemperatureHandler.class);

    public static void onLevelTick(TickEvent.Level event){
        tickCounter++;
        // 2 틱마다 실행
        if (tickCounter % 2 == 0) {
            Season.SubSeason subSeason = SeasonHelper.getSeasonState(event.getLevel()).getSubSeason();
            SeasonSavedData seasonSavedData = getSeasonSavedData(event.getLevel());
            TemperatureSavedData savedData = getTemperatureSavedData(event.getLevel());
            if(subSeason.equals(Season.SubSeason.EARLY_SPRING)){
                for (Player player : event.getLevel().players()) { // 모든 플레이어 반복
                    spawnFlameParticle((ServerLevel) event.getLevel(), player.position());
                }
            }
            if(seasonSavedData.seasonCycleTicks != 0){
                updateSavedData(event.getLevel(), savedData, seasonSavedData.seasonCycleTicks, subSeason);
            }
            log.info(String.valueOf(seasonSavedData.seasonCycleTicks));
        }

        // 메모리 관리를 위해 카운터 리셋 (선택적)
        if (tickCounter >= Integer.MAX_VALUE) {
            tickCounter = 0;
        }
    }

    public static float getTemp(Level level){
        TemperatureSavedData savedData = getTemperatureSavedData(level);
        return savedData.temperature;
    }

    private static float calculateTemperature(Season.SubSeason subSeason) {
        switch (subSeason) {
            case EARLY_SPRING:
                return 10.0F; // 초봄, 약간 쌀쌀한 기온
            case MID_SPRING:
                return 15.0F; // 중간 봄, 온화한 기온
            case LATE_SPRING:
                return 18.0F; // 늦봄, 다소 따뜻한 기온

            case EARLY_SUMMER:
                return 22.0F; // 초여름, 따뜻한 기온
            case MID_SUMMER:
                return 28.0F; // 한여름, 더운 기온
            case LATE_SUMMER:
                return 25.0F; // 늦여름, 다소 시원해짐

            case EARLY_AUTUMN:
                return 18.0F; // 초가을, 선선한 기온
            case MID_AUTUMN:
                return 15.0F; // 중간 가을, 온화한 기온
            case LATE_AUTUMN:
                return 10.0F; // 늦가을, 쌀쌀한 기온

            case EARLY_WINTER:
                return 5.0F; // 초겨울, 추운 기온
            case MID_WINTER:
                return 0.0F; // 한겨울, 매우 추운 기온
            case LATE_WINTER:
                return -5.0F; // 늦겨울, 차가운 기온

            default:
                return 0.0F; // 기본값, 예기치 않은 값이 들어올 경우
        }
    }

    public static void updateSavedData(Level level ,TemperatureSavedData savedData, int ticks, Season.SubSeason subSeason){
        log.info("duration"+(SeasonTime.ZERO.getDayDuration()));
        if(ticks % SeasonTime.ZERO.getDayDuration() ==0){
            switch (subSeason){
                case EARLY_SPRING -> {
                    savedData.temperature = ((-2.0f) + ((11.0f + 2.0f) * level.random.nextFloat()));
                    break;
                }
                case MID_SPRING ->{
                    savedData.temperature = ((4.0f) + ((18.0f - 4.0f) * level.random.nextFloat()));
                    break;
                }
                case LATE_SPRING ->{
                    savedData.temperature = ((10.0f) + ((24.0f - 10.0f) * level.random.nextFloat()));
                    break;
                }
                case EARLY_SUMMER -> {
                    savedData.temperature = ((17.0f) + ((27.0f - 17.0f) * level.random.nextFloat()));
                    break;
                }
                case MID_SUMMER ->{
                    savedData.temperature = ((21.0f) + ((29.0f - 21.0f) * level.random.nextFloat()));
                    break;
                }
                case LATE_SUMMER ->{
                    int heatWaveValue =  level.random.nextInt(3);
                    if(heatWaveValue == 0){
                        Component text = Component.translatable( "폭염!").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000)));
                        level.players().forEach(player ->
                                player.sendSystemMessage(text)
                        );
                        savedData.temperature = 35.0f;
                        savedData.setDirty();
                    }else {
                        savedData.temperature = ((21.0f) + ((30.0f - 21.0f) * level.random.nextFloat()));
                    }
                    log.info("{}summer", savedData.temperature);
                    break;
                }

                case EARLY_AUTUMN -> {
                    savedData.temperature = ((15.0f) + ((26.0f - 15.0f) * level.random.nextFloat()));
                    break;
                }
                case MID_AUTUMN ->{
                    savedData.temperature = ((6.0f) + ((20.0f - 6.0f) * level.random.nextFloat()));
                    break;
                }
                case LATE_AUTUMN ->{
                    savedData.temperature = ((-1.0f) + ((11.0f + 1.0f) * level.random.nextFloat()));
                    break;
                }
                case EARLY_WINTER -> {
                    savedData.temperature = ((-9.0f) + ((3.0f + 9.0f) * level.random.nextFloat()));
                    break;
                }
                case MID_WINTER ->{
                    savedData.temperature = ((-12.0f) + ((2.0f + 12.0f) * level.random.nextFloat()));
                    break;
                }
                case LATE_WINTER ->{
                    savedData.temperature = ((-8.0f) + ((5.0f + 8.0f) * level.random.nextFloat()));
                    break;
                }
            }
            log.debug( "temp"+(savedData.temperature));
        }
        savedData.setDirty();
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
                savedData.temperature = (world.random.nextInt(12)) * SeasonTime.ZERO.getSubSeasonDuration();
            }

            if (startingSeason > 0)
            {
                savedData.temperature = (startingSeason - 1) * SeasonTime.ZERO.getSubSeasonDuration();
            }

            savedData.setDirty(); //Mark for saving
            return savedData;
        };

        return saveDataManager.computeIfAbsent(new SavedData.Factory<>(defaultSaveDataSupplier, TemperatureSavedData::load, DataFixTypes.LEVEL), TemperatureSavedData.DATA_IDENTIFIER);
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
    public static void spawnFlameParticle(ServerLevel level, Vec3 vec) {

        var blockState = Blocks.SAND.defaultBlockState();

        var particle = new BlockParticleOption(ParticleTypes.FALLING_DUST, blockState);
        level.sendParticles(
                particle,// 블록 파티클 사용
                vec.x() + 0.5, vec.y() + 0.5, vec.z() + 0.5, // 위치
                100, // 파티클 개수
                16.0, 16.0, 16.0, // X, Y, Z 방향으로 퍼짐
                1 // 속도
        );
    }
}
