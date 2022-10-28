package uk.co.jcox.itemtest.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.system.NonnullDefault;
import org.lwjgl.system.windows.INPUT;
import uk.co.jcox.itemtest.setup.Registration;

import javax.annotation.Nonnull;

public class TradingboxBE extends BlockEntity {

    //CONFIG
    private static final int MAX_TIME = 50;
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private final ItemStackHandler itemHandler = createHandler(); //Internal Uses
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler); //Exposed

    private int counter;
    public TradingboxBE(BlockPos pos, BlockState state) {
        super(Registration.TRADINGBOX_BE.get(), pos, state);
    }

    public void tickServer() {
        //Check if there is an item present in index slot 0
        ItemStack stack = itemHandler.getStackInSlot(INPUT_SLOT);
        if(! (stack.getItem().equals(Items.AIR))) {
            //The item present is not air, so increase the counter
            counter++;
        }

        //The maximum timer is set to 200 ticks.
        //This means once reached, delete the item, and place a new item
        if(counter >= MAX_TIME) {
            itemHandler.extractItem(INPUT_SLOT, 1, false);
            itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(Items.BLUE_CONCRETE_POWDER), false);
            counter = 0;
            //Inventory has changed so mark block dirty so it can be saved.
            setChanged();
        }
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(INPUT_SLOT == slot) {
                    return true;
                }

                //todo need to setup two different capapabiltiies to allow the output slot to now be takable by the player
                return true;
            }
        };
    }


    @Override
    public void load(CompoundTag tag) {
        if(tag.contains("Inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("Inventory"));
        }

        super.load(tag);
    }

    public void saveAdditional(CompoundTag tag) {
        tag.put("Inventory", itemHandler.serializeNBT());
    }


    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }
}
