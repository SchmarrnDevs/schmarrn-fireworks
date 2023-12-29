package dev.schmarrn.schmarrnfireworks;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class SchmarrnFireworkRocketRecipe extends CustomRecipe {
    private static final Ingredient PAPER_INGREDIENT = Ingredient.of(Items.PAPER);
    private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of(Items.GUNPOWDER);
    private static final Ingredient STAR_INGREDIENT = Ingredient.of(Items.FIREWORK_STAR);
    private static final Ingredient CHEST_INGREDIENT = Ingredient.of(Items.CHEST);

    public SchmarrnFireworkRocketRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        boolean onlyOnePaper = false;
        boolean onlyOneChest = false;
        int i = 0;
        for (int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            ItemStack itemStack = craftingContainer.getItem(j);
            if (itemStack.isEmpty()) continue;
            if (PAPER_INGREDIENT.test(itemStack)) {
                if (onlyOnePaper) {
                    return false;
                }
                onlyOnePaper = true;
                continue;
            }
            if (CHEST_INGREDIENT.test(itemStack)) {
                if (onlyOneChest) {
                    return false;
                }
                onlyOneChest = true;
                continue;
            }
            if (!(GUNPOWDER_INGREDIENT.test(itemStack) ? ++i > 3 : !STAR_INGREDIENT.test(itemStack))) continue;
            return false;
        }
        return onlyOnePaper && onlyOneChest && i >= 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack itemStack = new ItemStack(SchmarrnFireworks.FIREWORK_ROCKET, 3);
        CompoundTag compoundTag = itemStack.getOrCreateTagElement("Fireworks");
        ListTag listTag = new ListTag();
        int i = 0;
        for (int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            CompoundTag compoundTag2;
            ItemStack itemStack2 = craftingContainer.getItem(j);
            if (itemStack2.isEmpty()) continue;
            if (GUNPOWDER_INGREDIENT.test(itemStack2)) {
                ++i;
                continue;
            }
            if (!STAR_INGREDIENT.test(itemStack2) || (compoundTag2 = itemStack2.getTagElement("Explosion")) == null) continue;
            listTag.add(compoundTag2);
        }
        compoundTag.putByte("Flight", (byte)i);
        if (!listTag.isEmpty()) {
            compoundTag.put("Explosions", listTag);
        }
        return itemStack;
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
