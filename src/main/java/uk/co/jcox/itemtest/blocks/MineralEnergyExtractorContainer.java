package uk.co.jcox.itemtest.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.RegistryObject;
import uk.co.jcox.itemtest.setup.Registration;


public class MineralEnergyExtractorContainer extends AbstractContainerMenu {

    private BlockEntity tile;
    private Player player;
    private IItemHandler playerInventory;
    private ContainerData mineralExtractorData;

    public MineralEnergyExtractorContainer(int windowId, BlockPos pos, Inventory playerInv, Player player, ContainerData data) {
        super(Registration.MINERAL_EXTRACTOR_CONTAINER.get(), windowId);
        this.player = player;
        this.tile = player.getCommandSenderWorld().getBlockEntity(pos);
        this.playerInventory = new InvWrapper(playerInv);
        this.mineralExtractorData = data;
        this.addDataSlots(mineralExtractorData);

        if(tile != null) {
            tile.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 16, 27));
            });
            addPlayerInventorySlots(playerInventory, 8, 85, 27, 18, 18);
        }
    }

    public MineralEnergyExtractorContainer(int windowId, BlockPos pos, Inventory inventory, Player player) {
        this(windowId, pos, inventory, player, new SimpleContainerData(1));
    }

    public int getEnergy() {
        return this.mineralExtractorData.get(0);
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
    public boolean stillValid(Player p_38874_) {
        return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), player, Registration.MINERAL_EXTRACTOR_BLOCK.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }
}
