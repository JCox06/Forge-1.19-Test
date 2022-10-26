package uk.co.jcox.itemtest.setup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import uk.co.jcox.itemtest.blocks.TradingboxBlock;
import uk.co.jcox.itemtest.blocks.TradingboxBE;
import uk.co.jcox.itemtest.blocks.TradingboxContainer;

import static uk.co.jcox.itemtest.ItemTest.MODID;

public class Registration {

//Registries
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

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
        BLOCK_ENTITIES.register(bus);
        CONTAINERS.register(bus);
    }


//Blocks
    public static final RegistryObject<Block> CHROMIUM_ORE_OVERWORLD = BLOCKS.register("chromium_ore_overworld", () -> new Block(ORE_PROPERTIES));
    public static final RegistryObject<Item> CHROMIUM_ORE_OVERWORLD_ITEM = fromBlock(CHROMIUM_ORE_OVERWORLD);

    public static final RegistryObject<Block> CHROMIUM_ORE_NETHER = BLOCKS.register("chromium_ore_nether", () -> new Block(ORE_PROPERTIES));
    public static final RegistryObject<Item> CHROMIUM_ORE_NETHER_ITEM = fromBlock(CHROMIUM_ORE_NETHER);

    public static final RegistryObject<Block> CHROMIUM_BLOCK = BLOCKS.register("chromium_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL)));
    public static final RegistryObject<Item> CHROMIUM_BLOCK_ITEM = fromBlock(CHROMIUM_BLOCK);

//BLOCK ENTITIES
    public static final RegistryObject<TradingboxBlock> TRADINGBOX_BLOCK = BLOCKS.register("tradingbox",() -> new TradingboxBlock());
    public static final RegistryObject<Item> TRADINGBOX_ITEM = fromBlock(TRADINGBOX_BLOCK);
    public static final RegistryObject<BlockEntityType<TradingboxBE>> TRADINGBOX_BE = BLOCK_ENTITIES.register("tradingbox", () ->
            BlockEntityType.Builder.of(TradingboxBE::new, TRADINGBOX_BLOCK.get()).build(null));
    public static final RegistryObject<MenuType<TradingboxContainer>> TRADINGBOX_CONTAINER = CONTAINERS.register("tradingbox", () ->
            IForgeMenuType.create((windowId, inv, data) -> new TradingboxContainer(windowId, data.readBlockPos(), inv, inv.player)));

//Items
    public static RegistryObject<Item> CHROMIUM_INGOT = ITEMS.register("chromium_ingot", () -> new Item(ITEM_PROPERTIES));
    public static RegistryObject<Item> RAW_CHROMIUM = ITEMS.register("raw_chromium", () -> new Item(ITEM_PROPERTIES));
}
