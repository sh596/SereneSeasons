package sereneseasons.season;

import glitchcore.event.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class TemperatureHandler {


    private static final Logger log = LoggerFactory.getLogger(TemperatureHandler.class);

    public static void onLevelTick(TickEvent.Level event){
        Season.SubSeason subSeason = SeasonHelper.getSeasonState(event.getLevel()).getSubSeason();
        log.debug("{}", calculateTemperature(subSeason));
    }

    public static float getTemp(Level level){
        Season.SubSeason subSeason = SeasonHelper.getSeasonState(level).getSubSeason();
        return calculateTemperature(subSeason);
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
}
