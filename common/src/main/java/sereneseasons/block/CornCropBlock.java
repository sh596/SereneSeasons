package sereneseasons.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
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
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import net.minecraft.world.item.ItemStack;

public class CornCropBlock extends CropBlock {
    public static final MapCodec<CornCropBlock> CODEC = simpleCodec(CornCropBlock::new);
    private static final VoxelShape[] SHAPE_BY_AGE;
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE;

    public CornCropBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            // 1개 또는 2개 씨앗을 드롭
            RandomSource random = level.getRandom();
            int seedCount = random.nextInt(2) + 1;
            popResource(level, pos, new ItemStack(getBaseSeedId(), seedCount));

            // 블록이 완전히 성장했으면 아이템 드롭
            if (state.getValue(getAgeProperty()) >= getMaxAge()) {
                popResource(level, pos, new ItemStack(SSItems.CORN, 1));
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    public boolean canSurvive(BlockState state, Level level, BlockPos pos) {
        // 부모 클래스의 기본 생존 조건 확인
        if (!super.canSurvive(state, level, pos)) {
            return false;
        }

        // 현재 계절이 여름인지 확인
        Season.SubSeason subSeason = SeasonHelper.getSeasonState(level).getSubSeason();
        return isSummer(subSeason);  // 여름에만 생존 가능
    }

    // 여름인지 확인하는 메서드
    private boolean isSummer(Season.SubSeason subSeason) {
        return subSeason == Season.SubSeason.EARLY_SUMMER ||
                subSeason == Season.SubSeason.MID_SUMMER ||
                subSeason == Season.SubSeason.LATE_SUMMER;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // 현재 계절 확인
        Season.SubSeason subSeason = SeasonHelper.getSeasonState(level).getSubSeason();

        if (!isSummer(subSeason)) {
            // 여름이 아닌 경우 일정 확률로 작물 파괴
            if (random.nextFloat() < 0.5f) {
                level.removeBlock(pos, false);  // 작물을 제거
            }
            return;
        }

        // 여름일 경우 정상적으로 성장
        super.randomTick(state, level, pos, random);
    }

    @Override
    public MapCodec<? extends CornCropBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return SSItems.CORN_SEED;
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
        AGE = BlockStateProperties.AGE_3;
        SHAPE_BY_AGE = new VoxelShape[]{
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)2.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)3.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)5.0F, (double)16.0F),
                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)7.0F, (double)16.0F)
//                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)8.0F, (double)16.0F),
//                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)9.0F, (double)16.0F),
//                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)9.0F, (double)16.0F),
//                Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)10.0F, (double)16.0F)
        };
    }
}

