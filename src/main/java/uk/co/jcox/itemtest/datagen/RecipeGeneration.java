package uk.co.jcox.itemtest.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.RegistryObject;
import uk.co.jcox.itemtest.setup.Registration;

import java.util.function.Consumer;

public class RecipeGeneration extends RecipeProvider {

    public RecipeGeneration(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.CHROMIUM_ORE_ITEM), Registration.CHROMIUM_INGOT.get(), 2.0f, 100)
                .unlockedBy("has_ore", has(Registration.CHROMIUM_ORE_ITEM))
                        .save(consumer, "chromium_ingot_from_smelting_chromium_ore");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.RAW_CHROMIUM.get()), Registration.CHROMIUM_INGOT.get(), 2.0f, 100)
                .unlockedBy("has_ore", has(Registration.CHROMIUM_ORE_ITEM))
                .save(consumer, "chromium_ingot_from_smelting_raw_chromium");

        ShapedRecipeBuilder.shaped(Registration.CHROMIUM_BLOCK.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Registration.CHROMIUM_INGOT.get())
                .unlockedBy("has_item", has(Registration.CHROMIUM_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(Registration.CHROMIUM_INGOT.get(), 9)
                .requires(Registration.CHROMIUM_BLOCK.get())
                .unlockedBy("has_item", has(Registration.CHROMIUM_BLOCK.get()))
                .save(consumer);

    }
}
