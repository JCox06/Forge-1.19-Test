package uk.co.jcox.itemtest.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import oshi.hardware.PowerSource;
import oshi.hardware.platform.unix.openbsd.OpenBsdPowerSource;

import java.util.List;

import static uk.co.jcox.itemtest.ItemTest.MODID;

public class TradingboxBlock extends Block implements EntityBlock {


    private static final String TRADINGBOX_TOOLTIP = "tooltip." + MODID + ".tradingbox";
    private static final String TRADINGBOX_DISPLAYNAME = "screen." + MODID + ".tradingbox";

    public TradingboxBlock() {
        super(Properties.of(
                Material.WOOD)
                .sound(SoundType.WOOD)
                .strength(1.0f)
                .lightLevel(s -> s.getValue(BlockStateProperties.LIT) ? 14 : 0)
                .requiresCorrectToolForDrops());
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> list, TooltipFlag flags) {
        list.add(Component.translatable(TRADINGBOX_TOOLTIP));
    }

    //For Entity Block
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new TradingboxBE(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(level.isClientSide) {
            return null;
        }

        return (lvl, pos, blockstate, t) -> {
            if( t instanceof  TradingboxBE tile) {
                tile.tickServer();
            }
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.LIT);
    }

    //Make it so the standard block state is unlit.
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(BlockStateProperties.LIT, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {

        //Ensure that this is the server (creation of GUIs should happen on the server only)
        if(!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof TradingboxBE) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable(TRADINGBOX_DISPLAYNAME);
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player playerEntity) {
                        return new TradingboxContainer(windowId, pos, inventory, playerEntity);
                    }
                };
                NetworkHooks.openScreen((ServerPlayer) player, containerProvider, entity.getBlockPos());
            } else {
                throw new IllegalStateException("Named Container Provider is Missing");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel p_221121_, T p_221122_) {
        return EntityBlock.super.getListener(p_221121_, p_221122_);
    }
}
