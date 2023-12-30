package dev.schmarrn.schmarrnfireworks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.schmarrn.schmarrnfireworks.SchmarrnFireworkRocketEntity;
import dev.schmarrn.schmarrnfireworks.SchmarrnFireworks;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
abstract public class CrossbowItemMixin extends ProjectileWeaponItem implements Vanishable{
    public CrossbowItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyExpressionValue(
            method = {"shootProjectile", "appendHoverText"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private static boolean schmarrnfireworks$isRocket(boolean original, @Local(ordinal = 1) ItemStack instance) {
        SchmarrnFireworks.LOGGER.info("is our Rocket? " + instance.is(SchmarrnFireworks.FIREWORK_ROCKET));
        if (instance.is(SchmarrnFireworks.FIREWORK_ROCKET)) {
            return true;
        }
        return original;
    }

    @ModifyReturnValue(
            method = "containsChargedProjectile",
            at = @At(value = "TAIL")
    )
    private static boolean schmarrnfireworks$containsChargedProjectile(boolean original, @Local ItemStack itemStack, @Local Item item) {
        return original ||
                (
                        item.equals(Items.FIREWORK_ROCKET)
                        && CrossbowItem.getChargedProjectiles(itemStack).stream().anyMatch(
                                (itemStackx) -> itemStackx.is(SchmarrnFireworks.FIREWORK_ROCKET)
                        )
                );
    }

    // Doesn't work, using redirect below
//    @WrapOperation(
//            method = "shootProjectile",
//            at = @At(
//                    value = "NEW",
//                    target = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;DDDZ)Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;"
//            )
//    )
//    private static FireworkRocketEntity schmarrnfireworks$SchamrrnFireworkRocketEntity(Level level, ItemStack itemStack2, Entity entity, double d, double e, double f, boolean bl, Operation<FireworkRocketEntity> original) {
//        if (itemStack2.is(SchmarrnFireworks.FIREWORK_ROCKET)) {
//            SchmarrnFireworks.LOGGER.info("replaced with our rocket");
//            return new SchmarrnFireworkRocketEntity(level, itemStack2, entity, d, e, f, bl);
//        } else {
//            return original.call(level, itemStack2, entity, d, e, f, bl);
//        }
//    }

    @Redirect(
            method = "shootProjectile",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;DDDZ)Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;"
            )
    )
    private static FireworkRocketEntity schmarrnfireworks$SchamrrnFireworkRocketEntity(Level level, ItemStack itemStack, Entity entity, double d, double e, double f, boolean bl) {
        if (itemStack.is(SchmarrnFireworks.FIREWORK_ROCKET)) {
            SchmarrnFireworks.LOGGER.info("replaced with our rocket");
            return new SchmarrnFireworkRocketEntity(level, itemStack, entity, d, e, f, bl);
        } else {
            return new FireworkRocketEntity(level, itemStack, entity, d, e, f, bl);
        }
    }
}
