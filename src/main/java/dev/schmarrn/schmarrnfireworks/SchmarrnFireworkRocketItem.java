package dev.schmarrn.schmarrnfireworks;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SchmarrnFireworkRocketItem extends FireworkRocketItem {
    // Rocket stuff
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

    // Bundle stuff
    private static final String TAG_ITEM = "Items";

    @Override
    public boolean isFoil(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG_ITEM);
    }
    private static Optional<ItemStack> remove(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEM)) {
            return Optional.empty();
        }
        ListTag listTag = compoundTag.getList(TAG_ITEM, 10);
        if (listTag.isEmpty()) {
            return Optional.empty();
        }
        ItemStack removedItem = ItemStack.of(listTag.getCompound(0));
        listTag.remove(0);
        itemStack.removeTagKey(TAG_ITEM);

        return Optional.of(removedItem);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack self, ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) {
            return false;
        }
        if (other.isEmpty()) {
            SchmarrnFireworkRocketItem.remove(self).ifPresent(itemStack -> {
                this.playRemoveOneSound(player);
                slotAccess.set(itemStack);
            });
        } else {
            int i = SchmarrnFireworkRocketItem.add(self, other);
            if (i > 0) {
                this.playInsertSound(player);
                other.shrink(i);
            }
        }
        return true;
    }

    private static int add(ItemStack self, ItemStack other) {
        if (other.isEmpty() || !other.getItem().canFitInsideContainerItems()) {
            return 0;
        }
        CompoundTag compoundTag = self.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEM)) {
            compoundTag.put(TAG_ITEM, new ListTag());
            ListTag listTag = compoundTag.getList(TAG_ITEM, 10);
            ItemStack oneOfOther = other.copyWithCount(1);
            CompoundTag itemTag = new CompoundTag();
            oneOfOther.save(itemTag);
            listTag.add(0, itemTag);
            return 1;
        } else {
            return 0;
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    // Both
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        ListTag listTag;
        CompoundTag compoundTag = itemStack.getTagElement(TAG_FIREWORKS);
        if (compoundTag == null) {
            return;
        }
        if (compoundTag.contains(TAG_FLIGHT, 99)) {
            list.add(Component.translatable("item.minecraft.firework_rocket.flight").append(CommonComponents.SPACE).append(String.valueOf(compoundTag.getByte(TAG_FLIGHT))).withStyle(ChatFormatting.GRAY));
        }
        if (!(listTag = compoundTag.getList(TAG_EXPLOSIONS, 10)).isEmpty()) {
            for (int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag2 = listTag.getCompound(i);
                ArrayList<Component> list2 = Lists.newArrayList();
                FireworkStarItem.appendHoverText(compoundTag2, list2);
                if (list2.isEmpty()) continue;
                for (int j = 1; j < list2.size(); ++j) {
                    list2.set(j, Component.literal("  ").append((Component)list2.get(j)).withStyle(ChatFormatting.GRAY));
                }
                list.addAll(list2);
            }
        }
    }
}
