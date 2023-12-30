package dev.schmarrn.schmarrnfireworks;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class SchmarrnFireworkRocketRecipe extends CustomRecipe {
    private static final Ingredient ROCKET_INGREDIENT = Ingredient.of(Items.FIREWORK_ROCKET);
    private static final Ingredient CHEST_INGREDIENT = Ingredient.of(Items.CHEST);

    public SchmarrnFireworkRocketRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        boolean onlyOneRocket = false;
        boolean onlyOneChest = false;
        for (int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            ItemStack itemStack = craftingContainer.getItem(j);
            if (itemStack.isEmpty()) continue;
            if (ROCKET_INGREDIENT.test(itemStack)) {
                if (onlyOneRocket) {
                    return false;
                }
                onlyOneRocket = true;
                continue;
            }
            if (CHEST_INGREDIENT.test(itemStack)) {
                if (onlyOneChest) {
                    return false;
                }
                onlyOneChest = true;
                continue;
            }
            return false;
        }
        return onlyOneRocket && onlyOneChest;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack self = new ItemStack(SchmarrnFireworks.FIREWORK_ROCKET, 1);
        for (int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            ItemStack other = craftingContainer.getItem(j);
            if (other.isEmpty()) continue;
            if (ROCKET_INGREDIENT.test(other)) {
                CompoundTag otherTag = other.getTagElement("Fireworks");
                CompoundTag selfTag = self.getOrCreateTagElement("Fireworks");
                selfTag.merge(otherTag);
                break;
            }
        }
        return self;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SchmarrnFireworks.SCHMARRN_FIREWORK_ROCKET_RECIPE;
    }
}
