package dev.schmarrn.schmarrnfireworks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchmarrnFireworks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("schmarrn-fireworks");

    public static final String MODID = "schmarrn-fireworks";
    public static final double EXPLOSION_FORCE = 0.25;

    public static final Item FIREWORK_ROCKET = new SchmarrnFireworkRocketItem(new FabricItemSettings());

    @Override
    public void onInitialize() {
        LOGGER.info("Everything starts with a boom!");

        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MODID, "firework_rocket"), FIREWORK_ROCKET);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            for (byte b : FireworkRocketItem.CRAFTABLE_DURATIONS) {
                ItemStack itemStack = new ItemStack(FIREWORK_ROCKET);
                FireworkRocketItem.setDuration(itemStack, b);
                output.accept(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        });
    }
}
