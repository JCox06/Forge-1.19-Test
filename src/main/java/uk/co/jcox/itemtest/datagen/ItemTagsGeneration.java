package uk.co.jcox.itemtest.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import uk.co.jcox.itemtest.ItemTest;
import uk.co.jcox.itemtest.setup.Registration;

public class ItemTagsGeneration extends ItemTagsProvider {

    public ItemTagsGeneration(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(generator, blockTags, ItemTest.MODID, helper);
    }


    @Override
    public void addTags() {
        tag(Tags.Items.ORES)
                .add(Registration.CHROMIUM_ORE_OVERWORLD_ITEM.get())
                .add(Registration.CHROMIUM_ORE_NETHER_ITEM.get());

        tag(Tags.Items.INGOTS)
                .add(Registration.CHROMIUM_INGOT.get());

        tag(Tags.Items.RAW_MATERIALS)
                .add(Registration.RAW_CHROMIUM.get());
        tag(Registration.CHROMIUM_ORE_ITEM)
                .add(Registration.CHROMIUM_ORE_OVERWORLD_ITEM.get())
                .add(Registration.CHROMIUM_ORE_NETHER_ITEM.get());
    }
}
