package uk.co.jcox.itemtest.setup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static uk.co.jcox.itemtest.ItemTest.MODID;

public class Registration {

//Registries
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

//Tags
    public static final TagKey<Block> CHROMIUM_ORE = BlockTags.create(new ResourceLocation(MODID, "chromium_ore"));
    public static final TagKey<Item> CHROMIUM_ORE_ITEM = ItemTags.create(new ResourceLocation(MODID, "chromium_ore_item"));

//Other
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);
    public static final BlockBehaviour.Properties ORE_PROPERTIES = BlockBehaviour.Properties.of(Material.STONE).strength(2f);

    private static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }

//Registration
    public static final void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }


//Blocks
    public static RegistryObject<Block> CHROMIUM_ORE_OVERWORLD = BLOCKS.register("chromium_ore_overworld", () -> new Block(ORE_PROPERTIES));
    public static RegistryObject<Item> CHROMIUM_ORE_OVERWORLD_ITEM = fromBlock(CHROMIUM_ORE_OVERWORLD);

    public static RegistryObject<Block> CHROMIUM_ORE_NETHER = BLOCKS.register("chromium_ore_nether", () -> new Block(ORE_PROPERTIES));
    public static RegistryObject<Item> CHROMIUM_ORE_NETHER_ITEM = fromBlock(CHROMIUM_ORE_NETHER);

    public static RegistryObject<Block> CHROMIUM_BLOCK = BLOCKS.register("chromium_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL)));
    public static RegistryObject<Item> CHROMIUM_BLOCK_ITEM = fromBlock(CHROMIUM_BLOCK);

//Items
    public static RegistryObject<Item> CHROMIUM_INGOT = ITEMS.register("chromium_ingot", () -> new Item(ITEM_PROPERTIES));
    public static RegistryObject<Item> RAW_CHROMIUM = ITEMS.register("raw_chromium", () -> new Item(ITEM_PROPERTIES));
}
