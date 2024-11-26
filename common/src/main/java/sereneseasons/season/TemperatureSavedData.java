package sereneseasons.season;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.SavedData;

public class TemperatureSavedData extends SavedData {
    public static final String DATA_IDENTIFIER = "temperature";
    public float temperature;

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider)
    {
        nbt.putFloat("temperature",  this.temperature);
        return nbt;
    }

    public static TemperatureSavedData load(CompoundTag nbt, HolderLookup.Provider provider)
    {
        TemperatureSavedData data = new TemperatureSavedData();
        data.temperature = Mth.clamp(nbt.getFloat("temperature"), 0, SeasonTime.ZERO.getCycleDuration());
        return data;
    }

}
