/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package sereneseasons.fabric.core;

import glitchcore.fabric.GlitchCoreInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import sereneseasons.api.SSBlocks;
import sereneseasons.core.SereneSeasons;
import sereneseasons.init.ModClient;

public class SereneSeasonsFabric implements GlitchCoreInitializer
{
    @Override
    public void onInitialize()
    {
        SereneSeasons.init();
    }

    @Override
    public void onInitializeClient()
    {
        ModClient.setup();
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.CABBAGE_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.CHILIPEPPER_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.CHIVES_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.CORN_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.CUCUMBER_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.GARLIC_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.NAPACABBAGE_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.RADISH_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.SPINACH_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SSBlocks.TOMATO_CROP, RenderType.cutout());
    }
}
