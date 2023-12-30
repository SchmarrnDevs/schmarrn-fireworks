package dev.schmarrn.schmarrnfireworks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchmarrnFireworks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("schmarrn-fireworks");

    public static final String MODID = "schmarrn-fireworks";

    public static final Item FIREWORK_ROCKET = new SchmarrnFireworkRocketItem(new FabricItemSettings().maxCount(1));

    public static final RecipeSerializer<SchmarrnFireworkRocketRecipe> SCHMARRN_FIREWORK_ROCKET_RECIPE = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(MODID, "custom_rocket_recipe"), new SimpleCraftingRecipeSerializer<>(SchmarrnFireworkRocketRecipe::new));

    public static final EntityType<SchmarrnFireworkRocketEntity> ENTITY_TYPE = Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(MODID, "firework_rocket_et"), EntityType.Builder.<SchmarrnFireworkRocketEntity>of(SchmarrnFireworkRocketEntity::new, MobCategory.MISC).build(""));

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

        // Dispenser
        DispenserBlock.registerBehavior(FIREWORK_ROCKET, new DefaultDispenseItemBehavior(){
            @Override
            public ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
                Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
                Vec3 vec3 = DispenseItemBehavior.getEntityPokingOutOfBlockPos(blockSource, EntityType.FIREWORK_ROCKET, direction);
                FireworkRocketEntity fireworkRocketEntity = new SchmarrnFireworkRocketEntity((Level)blockSource.level(), itemStack, vec3.x(), vec3.y(), vec3.z(), true);
                fireworkRocketEntity.shoot(direction.getStepX(), direction.getStepY(), direction.getStepZ(), 0.5f, 1.0f);
                blockSource.level().addFreshEntity(fireworkRocketEntity);
                itemStack.shrink(1);
                return itemStack;
            }

            @Override
            protected void playSound(BlockSource blockSource) {
                blockSource.level().levelEvent(1004, blockSource.pos(), 0);
            }
        });

        // Crossbow
        ProjectileWeaponItem.ARROW_OR_FIREWORK = ProjectileWeaponItem.ARROW_OR_FIREWORK.or((itemStack) -> itemStack.is(FIREWORK_ROCKET));
    }
}
