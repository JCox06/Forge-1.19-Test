package uk.co.jcox.itemtest.datagen;


import com.mojang.logging.LogUtils;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import uk.co.jcox.itemtest.ItemTest;



@Mod.EventBusSubscriber(modid = ItemTest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        LOGGER.info("=====GENERATING DATA=====");
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        //Loot table, Recipe, Tags, Advancements
            generator.addProvider(event.includeServer(), new RecipeGeneration(generator));
            BlockTagsGeneration blockTags = new BlockTagsGeneration(generator, fileHelper);
            generator.addProvider(event.includeServer(), blockTags);
            generator.addProvider(event.includeServer(), new ItemTagsGeneration(generator, blockTags, fileHelper));

        //Translations, sounds, models, blockstates
            generator.addProvider(event.includeClient(), new BlockStatesGeneration(generator, fileHelper));
            generator.addProvider(event.includeClient(), new ModelGeneration(generator, fileHelper));


    }

}
