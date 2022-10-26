package uk.co.jcox.itemtest.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
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
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < slotsPerCol; j++) {
                addSlot(new SlotItemHandler(handler, invIndex, x, y));
                x += changeInX;
                invIndex++;
            }
            x = startingX;
            y += changeInY;
        }

        //Hotbar
        int hotbarEndIndex = 8;
        for(int i = 0; i < hotbarEndIndex; i++) {
            addSlot(new SlotItemHandler(handler, i, startingX, y));
            startingX += changeInX;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        //todo needs implementing
        return null;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, Registration.TRADINGBOX_BLOCK.get());
    }
}
