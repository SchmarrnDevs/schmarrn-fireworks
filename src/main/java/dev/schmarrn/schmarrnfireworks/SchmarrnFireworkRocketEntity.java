package dev.schmarrn.schmarrnfireworks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SchmarrnFireworkRocketEntity extends FireworkRocketEntity {
    public SchmarrnFireworkRocketEntity(EntityType<? extends FireworkRocketEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SchmarrnFireworkRocketEntity(Level level, double d, double e, double f, ItemStack itemStack) {
        super(level, d, e, f, itemStack);
    }

    public SchmarrnFireworkRocketEntity(Level level, @Nullable Entity entity, double d, double e, double f, ItemStack itemStack) {
        super(level, entity, d, e, f, itemStack);
    }

    public SchmarrnFireworkRocketEntity(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        super(level, itemStack, livingEntity);
    }

    public SchmarrnFireworkRocketEntity(Level level, ItemStack itemStack, double d, double e, double f, boolean bl) {
        super(level, itemStack, d, e, f, bl);
    }

    public SchmarrnFireworkRocketEntity(Level level, ItemStack itemStack, Entity entity, double d, double e, double f, boolean bl) {
        super(level, itemStack, entity, d, e, f, bl);
    }

    @Override
    public void explode() {
        super.explode();
        Level level = this.level();
        Vec3 pos = this.position();

        Random rand = new Random();

        if (!level.isClientSide) {
            ItemEntity itementity = new ItemEntity(
                    level,
                    pos.x, pos.y, pos.z,
                    new ItemStack(Items.DIAMOND, 1),
                    rand.nextDouble(-SchmarrnFireworks.EXPLOSION_FORCE, SchmarrnFireworks.EXPLOSION_FORCE),
                    rand.nextDouble(SchmarrnFireworks.EXPLOSION_FORCE),
                    rand.nextDouble(-SchmarrnFireworks.EXPLOSION_FORCE, SchmarrnFireworks.EXPLOSION_FORCE)
            );
            level.addFreshEntity(itementity);
        }
    }
}
