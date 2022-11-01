package uk.co.jcox.itemtest.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Entity;

import java.awt.*;
import java.util.List;

import static uk.co.jcox.itemtest.ItemTest.MODID;

public class MineralEnergyExtractorBlock extends Block implements EntityBlock {

    private static final String TOOLTIP = "tooltip." + MODID + ".mineral_energy_extractor";
    private static final VoxelShape RENDER_SHAPE = Shapes.box(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);

    public MineralEnergyExtractorBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .lightLevel(state -> state.getValue(BlockStateProperties.POWERED) ? 5 : 0)
                .sound(SoundType.AMETHYST)
                .strength(6.0f)
                .requiresCorrectToolForDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MineralEnergyExtractorBE(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        if(level.isClientSide()) {
            return null;
        }

        return (lvl, pos, blockstate, t) -> {
            if(t instanceof MineralEnergyExtractorBE) {
                MineralEnergyExtractorBE be = (MineralEnergyExtractorBE) t;
                be.tickServer();
            }
        };
    }


    @Override
    public void appendHoverText(ItemStack p_49816_, @Nullable BlockGetter p_49817_, List<Component> list, TooltipFlag p_49819_) {
        list.add(Component.translatable(TOOLTIP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.POWERED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return super.getStateForPlacement(p_49820_);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState p_60578_, BlockGetter p_60579_, BlockPos p_60580_) {
        return RENDER_SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        //Opening containers is ONLY done on the client
        if (! (level.isClientSide())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MineralEnergyExtractorBE be) {
                NetworkHooks.openScreen((ServerPlayer) player, be, blockEntity.getBlockPos());
            } else {
                throw new IllegalStateException();
            }
        }
        return InteractionResult.SUCCESS;
    }
}
