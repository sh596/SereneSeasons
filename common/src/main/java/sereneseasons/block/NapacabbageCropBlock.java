package sereneseasons.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sereneseasons.api.SSItems;

public class NapacabbageCropBlock extends CropBlock {
    public static final MapCodec<NapacabbageCropBlock> CODEC = simpleCodec(NapacabbageCropBlock::new);
    private static final VoxelShape[] SHAPE_BY_AGE;
    public static final int MAX_AGE = 7;
    public static final IntegerProperty AGE;

    public NapacabbageCropBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public MapCodec<? extends NapacabbageCropBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return SSItems.NAPACABBAGE_SEED;
    }

    @Override
    protected VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE_BY_AGE[this.getAge($$0)];
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(new Property[]{AGE});
    }

    static {
        AGE = BlockStateProperties.AGE_7;
        SHAPE_BY_AGE = new VoxelShape[]{
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)2.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)3.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)5.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)7.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)8.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)9.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)9.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)10.0F, (double)16.0F)
        };
    }
}

