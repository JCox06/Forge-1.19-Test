package uk.co.jcox.itemtest.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.jcox.itemtest.setup.Registration;
import uk.co.jcox.itemtest.varia.CustomEnergyStorage;

import java.util.Random;

import static uk.co.jcox.itemtest.ItemTest.MODID;

public class MineralEnergyExtractorBE extends BlockEntity implements MenuProvider {

    private static final String SCREEN = "screen." + MODID + ".mineral_energy_extractor";

    private static final int ENERGY_CAPACITY = 80000; //Maximum Energy Capacity
    private static final int ENERGY_GENERATION = 150; //Energy released per tick
    private static final int ENERGY_SEND = 500; //Energy sent during tick

    //Power of mineral metal compound
    private static final int MINERAL_METAL_COMPOUND_POWER = 15;

    private final ItemStackHandler itemStackHandler = createItemStackHandler();
    private final LazyOptional<IItemHandler> LItemHandler = LazyOptional.of(() -> itemStackHandler);

    private int counter;

    //todo save this block when the energy storage changes
    private final CustomEnergyStorage energyHandler = createEnergyHandler();
    private final LazyOptional<IEnergyStorage> LEnergyStorage = LazyOptional.of(() -> energyHandler);


    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == 0) {
                return MineralEnergyExtractorBE.this.energyHandler.getEnergyStored();
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                MineralEnergyExtractorBE.this.energyHandler.setEnergy(value);
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };


    public MineralEnergyExtractorBE(BlockPos pos, BlockState state) {
        super(Registration.MINERAL_EXTRACTOR_BE.get(), pos, state);
    }

    public void tickServer() {

        if (counter > 0) {
            energyHandler.addEnergy(ENERGY_GENERATION);
            counter--;
        }

        if (counter <= 0) {
            ItemStack stack = itemStackHandler.getStackInSlot(0);
            if(stack.getItem().equals(Items.AIR)) {
                return;
            }
            itemStackHandler.extractItem(0, 1, false);
            counter = MINERAL_METAL_COMPOUND_POWER;
        }

        BlockState state = level.getBlockState(worldPosition);
        if(state.getValue(BlockStateProperties.POWERED) != counter > 0) {
            level.setBlock(worldPosition, state.setValue(BlockStateProperties.POWERED, counter > 0), Block.UPDATE_ALL);
        }

        sendPower();

    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        LItemHandler.invalidate();
        LEnergyStorage.invalidate();
    }

    private ItemStackHandler createItemStackHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.getItem().equals(Registration.METAL_MINERAL_COMPUND.get());
            }
        };
    }

    private CustomEnergyStorage createEnergyHandler() {
        return new CustomEnergyStorage(ENERGY_CAPACITY, 0) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    private void sendPower() {
        if(energyHandler.getEnergyStored() == 0) {
            return;
        }

        for (Direction direction: Direction.values()) {
            BlockEntity adjacentBlockEntity = level.getBlockEntity(worldPosition.relative(direction));
            if(adjacentBlockEntity == null) {
                return;
            }
            adjacentBlockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).map(handler -> {
                if(handler.canReceive()) {
                    //Return the minimum of the following numbers
                    int recieved = handler.receiveEnergy(Math.min(ENERGY_CAPACITY, ENERGY_SEND), false);
                    energyHandler.consumeEnergy(recieved);
                    setChanged();
                    return energyHandler.getEnergyStored() > 0;
                }
                return true;
            });
        }
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("Inventory")) {
            itemStackHandler.deserializeNBT(tag.getCompound("Inventory"));
        }
        if(tag.contains("Energy")) {
            itemStackHandler.deserializeNBT(tag.getCompound("Energy"));
        }
        if(tag.contains("Info")) {
            counter = tag.getCompound("Info").getInt("Counter");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("Inventory", itemStackHandler.serializeNBT());
        tag.put("Energy", itemStackHandler.serializeNBT());
        CompoundTag infoTag = new CompoundTag();
        infoTag.putInt("Counter", counter);
        tag.put("Info", infoTag);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return LItemHandler.cast();
        }
        if(cap == ForgeCapabilities.ENERGY) {
            return LEnergyStorage.cast();
        }

        return super.getCapability(cap, side);
    }



    @Override
    public Component getDisplayName() {
        return Component.translatable(SCREEN);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player plr) {
        return new MineralEnergyExtractorContainer(id, this.getBlockPos(), inv, plr, dataAccess);
    }
}
