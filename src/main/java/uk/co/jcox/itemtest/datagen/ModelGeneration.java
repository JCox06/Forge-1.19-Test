package uk.co.jcox.itemtest.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import uk.co.jcox.itemtest.setup.Registration;

import static uk.co.jcox.itemtest.ItemTest.MODID;

public class ModelGeneration extends ItemModelProvider {

    public ModelGeneration(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Blocks
        withExistingParent(Registration.CHROMIUM_ORE_OVERWORLD_ITEM.getId().getPath(), modLoc("block/chromium_ore_overworld"));
        withExistingParent(Registration.CHROMIUM_ORE_NETHER_ITEM.getId().getPath(), modLoc("block/chromium_ore_nether"));
        withExistingParent(Registration.CHROMIUM_BLOCK.getId().getPath(), modLoc("block/chromium_block"));
        withExistingParent(Registration.TRADINGBOX_ITEM.getId().getPath(), modLoc("block/tradingbox"));

        //Items
        singleTexture(Registration.CHROMIUM_INGOT.getId().getPath(), mcLoc("item/generated"),
                "layer0", modLoc("item/chromium_ingot"));

        singleTexture(Registration.RAW_CHROMIUM.getId().getPath(), mcLoc("item/generated"),
                "layer0", modLoc("item/raw_chromium"));

        singleTexture(Registration.STRANGE_MINERAL.getId().getPath(), mcLoc("item/generated"),
                "layer0", modLoc("item/strange_mineral"));

        singleTexture(Registration.METAL_MINERAL_COMPUND.getId().getPath(), mcLoc("item/generated"),
                "layer0", modLoc("item/metal_mineral_compound"));

    }
}
