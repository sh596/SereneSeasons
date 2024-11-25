/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package sereneseasons.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import sereneseasons.api.SSBlockEntities;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.block.entity.SeasonSensorBlockEntity;
import sereneseasons.init.ModConfig;
import sereneseasons.season.SeasonTime;

import javax.annotation.Nullable;

public class SeasonSensorBlock extends BaseEntityBlock
{
    public static final MapCodec<SeasonSensorBlock> CODEC = simpleCodec(SeasonSensorBlock::new);
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    public static final IntegerProperty SEASON = IntegerProperty.create("season", 0, 3);

    public SeasonSensorBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0).setValue(SEASON, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec()
    {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext selectionContext)
    {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state)
    {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter reader, BlockPos pos, Direction direction)
    {
        return state.getValue(POWER);
    }

    public void updatePower(Level world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);

        if (ModConfig.seasons.isDimensionWhitelisted(world.dimension()))
        {
            BlockState currentState = world.getBlockState(pos);

            int power = 0;
            int startTicks = currentState.getValue(SEASON) * SeasonTime.ZERO.getSeasonDuration();
            int endTicks = (currentState.getValue(SEASON) + 1) * SeasonTime.ZERO.getSeasonDuration();
            int currentTicks = SeasonHelper.getSeasonState(world).getSeasonCycleTicks();

            if (currentTicks >= startTicks && currentTicks <= endTicks)
            {
                float delta = (float)(currentTicks - startTicks) / (float)SeasonTime.ZERO.getSeasonDuration();
                power = (int)Math.min(delta * 15.0F + 1.0F, 15.0F);
            }

            //Only update the state if the power level has actually changed
            if ((currentState.getValue(POWER)).intValue() != power)
            {
                world.setBlock(pos, currentState.setValue(POWER, Integer.valueOf(power)), 3);
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult rayTraceResult)
    {
        if (player.mayBuild())
        {
            if (level.isClientSide)
            {
                return InteractionResult.SUCCESS;
            }
            else
            {
                BlockState blockstate = state.cycle(SEASON);
                level.setBlock(pos, blockstate, 4);
                updatePower(level, pos);
                return InteractionResult.SUCCESS;
            }
        }
        else
        {
            return super.useWithoutItem(state, level, pos, player, rayTraceResult);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isSignalSource(BlockState state)
    {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new SeasonSensorBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && level.dimensionType().hasSkyLight() ? createTickerHelper(type, (BlockEntityType<SeasonSensorBlockEntity>) SSBlockEntities.SEASON_SENSOR, SeasonSensorBlock::tickEntity) : null;
    }

    private static void tickEntity(Level level, BlockPos pos, BlockState state, SeasonSensorBlockEntity entity)
    {
        if (level != null && !level.isClientSide && SeasonHelper.getSeasonState(level).getSeasonCycleTicks() % 20L == 0L)
        {
            Block block = state.getBlock();
            if (block instanceof SeasonSensorBlock)
            {
                ((SeasonSensorBlock)block).updatePower(level, pos);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(POWER, SEASON);
    }
}
