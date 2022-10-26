package uk.co.jcox.itemtest.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import uk.co.jcox.itemtest.setup.Registration;

public class TradingboxContainer extends AbstractContainerMenu {

    private BlockEntity blockEntity;
    private Player playerEntity;
    private IItemHandler playerInventory;

    public TradingboxContainer(int windowId, BlockPos pos, Inventory playerInv, Player player) {
        super(Registration.TRADINGBOX_CONTAINER.get(), windowId);
        blockEntity = player.getCommandSenderWorld().getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInv);

        if(blockEntity != null) {
            blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
                //h = item handler of blockEntity
                addSlot(new SlotItemHandler(h, 0, 60, 45));
                addSlot(new SlotItemHandler(h, 1, 125, 45));
            });
        }

        addPlayerInventorySlots(playerInventory, 8, 85, 27, 18, 18);
    }


    private void addPlayerInventorySlots(IItemHandler handler, int startingX, int startingY, int slots, int changeInX, int changeInY) {
        int slotsPerCol = 9;
        int invIndex = 9;

        int x = startingX;
        int y = startingY;

        int cols = slots / slotsPerCol;

        //Inventory
        for(int i = 0; i < cols; i++) { //Horizontal (Rows)
            for(int j = 0; j < slotsPerCol; j++) { //Vertical (Columns)
                addSlot(new SlotItemHandler(handler, invIndex, x, y));
                x += changeInX;
                invIndex++;
            }
            x = startingX;
            y += changeInY;
        }

        //Hotbar
        int hotbarEndIndex = 9;
        for(int i = 0; i < hotbarEndIndex; i++) {
            addSlot(new SlotItemHandler(handler, i, startingX, y));
            startingX += changeInX;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        //Hotbar slots start from 2 - 11
        //Inventory Slots start from 12 - 29

        //Check if the player has clicked on an item stack
        Slot slot = this.slots.get(index);
        if(slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();

            //Check if the player has clicked on the output or input block
            if(index == 0 || index == 1) {
                this.moveItemStackTo(stack, 2, 38, true);
            } else {
                this.moveItemStackTo(stack, 0, 1, true);
            }

            return ItemStack.EMPTY;
        }

        return null;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, Registration.TRADINGBOX_BLOCK.get());
    }
}
