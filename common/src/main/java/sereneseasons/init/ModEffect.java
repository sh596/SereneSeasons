package sereneseasons.init;

import com.mojang.blaze3d.shaders.Effect;
import glitchcore.util.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import sereneseasons.api.SSBlocks;
import sereneseasons.api.SSItems;
import sereneseasons.core.SereneSeasons;
import sereneseasons.effect.ColdEffect;
import sereneseasons.effect.SSEffects;
import sereneseasons.item.*;

import java.util.function.BiConsumer;

import static sereneseasons.api.SSItems.*;
import static sereneseasons.api.SSItems.SEASON_SENSOR;

public class ModEffect {
    public static void setup(BiConsumer<ResourceLocation, MobEffect> func)
    {
        registerEffects(func);

        if (Environment.isClient())
        {
            ModClient.registerItemProperties();
        }
    }

    public static void registerEffects(BiConsumer<ResourceLocation, MobEffect> func)
    {
        // SS Creative Tab Icon
        SSEffects.Cold = register(func, "cold", new ColdEffect(MobEffectCategory.HARMFUL, 13458607));

    }


    private static MobEffect register(BiConsumer<ResourceLocation, MobEffect> func, String name, MobEffect effect)
    {
        func.accept(ResourceLocation.fromNamespaceAndPath(SereneSeasons.MOD_ID, name), effect);
        return effect;
    }

}
