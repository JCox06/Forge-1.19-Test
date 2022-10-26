package uk.co.jcox.itemtest.setup;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import uk.co.jcox.itemtest.blocks.TradingboxScreen;

public class ClientSetup {

    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Registration.TRADINGBOX_CONTAINER.get(), TradingboxScreen::new);
        });
    }
}
