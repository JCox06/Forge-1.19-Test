package uk.co.jcox.itemtest.setup;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import uk.co.jcox.itemtest.client.MineralEnergyExtractorScreen;
import uk.co.jcox.itemtest.client.TradingboxScreen;

import java.awt.*;

public class ClientSetup {

    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Registration.TRADINGBOX_CONTAINER.get(), TradingboxScreen::new);
            MenuScreens.register(Registration.MINERAL_EXTRACTOR_CONTAINER.get(), MineralEnergyExtractorScreen::new);
        });
    }
}
