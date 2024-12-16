/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package sereneseasons.fabric.core;

import glitchcore.fabric.GlitchCoreInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import sereneseasons.api.SSBlocks;
import sereneseasons.block.CabbageCropBlock;
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

    }
}
