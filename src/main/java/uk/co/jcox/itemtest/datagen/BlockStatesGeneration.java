package uk.co.jcox.itemtest.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import uk.co.jcox.itemtest.setup.Registration;

import static uk.co.jcox.itemtest.ItemTest.MODID;

public class BlockStatesGeneration extends BlockStateProvider {

    public BlockStatesGeneration(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.CHROMIUM_ORE_OVERWORLD.get());
        simpleBlock(Registration.CHROMIUM_ORE_NETHER.get());
        simpleBlock(Registration.CHROMIUM_BLOCK.get());
        simpleBlock(Registration.TRADINGBOX_BLOCK.get());
    }
}
