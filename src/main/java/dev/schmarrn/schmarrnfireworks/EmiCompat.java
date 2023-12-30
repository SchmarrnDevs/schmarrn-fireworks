package dev.schmarrn.schmarrnfireworks;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.List;

public class EmiCompat implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addRecipe(new EmiCraftingRecipe(List.of(EmiStack.of(Items.FIREWORK_ROCKET), EmiStack.of(Items.CHEST)),  EmiStack.of(SchmarrnFireworks.FIREWORK_ROCKET), new ResourceLocation(SchmarrnFireworks.MODID, "firework_rocket")));
    }
}
