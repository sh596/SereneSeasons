package sereneseasons.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import sereneseasons.api.SSBlocks;
import sereneseasons.block.*;
import sereneseasons.core.SereneSeasons;

import java.util.function.BiConsumer;

public class ModBlocks
{
    public static void registerBlocks(BiConsumer<ResourceLocation, Block> func)
    {
        SSBlocks.SEASON_SENSOR = register(func, new SeasonSensorBlock(Block.Properties.of().strength(0.2F).sound(SoundType.STONE)), "season_sensor");
        SSBlocks.NAPACABBAGE_CROP = register(func, new NapacabbageCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "napacabbage_crop");
        SSBlocks.TOMATO_CROP = register(func, new TomatoCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "tomato_crop");
        SSBlocks.CORN_CROP = register(func, new CornCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "corn_crop");
        SSBlocks.CHIVES_CROP = register(func, new ChivesCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "chives_crop");
        SSBlocks.CABBAGE_CROP = register(func, new CabbageCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "cabbage_crop");
        SSBlocks.CHILIPEPPER_CROP = register(func, new ChilipepperCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "chilipepper_crop");
        SSBlocks.SPINACH_CROP = register(func, new SpinachCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "spinach_crop");
        SSBlocks.GARLIC_CROP = register(func, new GarlicCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "garlic_crop");
        SSBlocks.CUCUMBER_CROP = register(func, new CucumberCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "cucumber_crop");
        SSBlocks.RADISH_CROP = register(func, new RadishCropBlock(Block.Properties.of().noCollission().noOcclusion().randomTicks().sound(SoundType.CROP)), "radish_crop");
    }

    private static Block register(BiConsumer<ResourceLocation, Block> func, Block block, String name)
    {
        func.accept(ResourceLocation.fromNamespaceAndPath(SereneSeasons.MOD_ID, name), block);
        return block;
    }
}
