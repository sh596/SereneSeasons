package sereneseasons.season;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.bossbar.BossBar;

public class PlayerTemperatureSavedData extends SavedData {
    private static final Logger log = LoggerFactory.getLogger(TemperatureSavedData.class);
    public static final String DATA_IDENTIFIER = "playerTemperature";
    //플레이어 온도 변화값 저장
    public float playerTemperature;

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider)
    {
        nbt.putFloat("playerTemperature",  this.playerTemperature);
        this.setDirty();
        return nbt;
    }

    public static PlayerTemperatureSavedData load(CompoundTag nbt, HolderLookup.Provider provider) {
        PlayerTemperatureSavedData data = new PlayerTemperatureSavedData();

        // NBT에 playerTemperature 값이 없으면 기본값 36.5f 사용
        if (nbt.contains("playerTemperature")) {
            data.playerTemperature = Mth.clamp(nbt.getFloat("playerTemperature"), 36.5f, SeasonTime.ZERO.getCycleDuration());
        } else {
            // NBT에 값이 없으면 기본값으로 설정
            data.playerTemperature = 36.5f;
        }

        return data;
    }

}
