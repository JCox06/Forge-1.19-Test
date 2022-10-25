package uk.co.jcox.itemtest.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import uk.co.jcox.itemtest.ItemTest;
import uk.co.jcox.itemtest.setup.Registration;

public class BlockTagsGeneration extends BlockTagsProvider {

    public BlockTagsGeneration(DataGenerator p_126511_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126511_, ItemTest.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {

        tag(Registration.CHROMIUM_ORE)
                .add(Registration.CHROMIUM_ORE_NETHER.get())
                .add(Registration.CHROMIUM_ORE_OVERWORLD.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.CHROMIUM_ORE_NETHER.get())
                .add(Registration.CHROMIUM_ORE_OVERWORLD.get());

        tag(Tags.Blocks.ORES)
                .add(Registration.CHROMIUM_ORE_NETHER.get())
                .add(Registration.CHROMIUM_ORE_OVERWORLD.get());

        tag(Tags.Blocks.STORAGE_BLOCKS)
                .add(Registration.CHROMIUM_BLOCK.get());


        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(Registration.CHROMIUM_ORE_NETHER.get())
                .add(Registration.CHROMIUM_ORE_OVERWORLD.get());

        tag(BlockTags.BEACON_BASE_BLOCKS)
                .add(Registration.CHROMIUM_BLOCK.get());

//        tag(BlockTags.MINEABLE_WITH_PICKAXE)
//                .addTags(Registration.CHROMIUM_ORE);
//
//        tag(Tags.Blocks.ORES)
//                .addTags(Registration.CHROMIUM_ORE);
//
//        tag(BlockTags.NEEDS_IRON_TOOL)
//                .addTags(Registration.CHROMIUM_ORE);

    }
}
