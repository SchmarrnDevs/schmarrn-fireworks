package dev.schmarrn.schmarrnfireworks;

import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SchmarrnFireworkRocketItem extends FireworkRocketItem {
    public SchmarrnFireworkRocketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        if (!level.isClientSide) {
            ItemStack itemStack = useOnContext.getItemInHand();
            Vec3 vec3 = useOnContext.getClickLocation();
            Direction direction = useOnContext.getClickedFace();
            FireworkRocketEntity fireworkRocketEntity = new SchmarrnFireworkRocketEntity(level, useOnContext.getPlayer(), vec3.x + (double)direction.getStepX() * 0.15, vec3.y + (double)direction.getStepY() * 0.15, vec3.z + (double)direction.getStepZ() * 0.15, itemStack);
            level.addFreshEntity(fireworkRocketEntity);
            itemStack.shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (player.isFallFlying()) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            if (!level.isClientSide) {
                FireworkRocketEntity fireworkRocketEntity = new SchmarrnFireworkRocketEntity(level, itemStack, player);
                level.addFreshEntity(fireworkRocketEntity);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
        }
        return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemStack = new ItemStack(this);
        FireworkRocketItem.setDuration(itemStack, (byte)1);
        return itemStack;
    }
}
