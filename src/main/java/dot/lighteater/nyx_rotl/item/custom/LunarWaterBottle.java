package dot.lighteater.nyx_rotl.item.custom;

import dot.lighteater.nyx_rotl.fluid.ModFluids;
import dot.lighteater.nyx_rotl.procedures.applyLunarWater;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;

import javax.annotation.Nullable;

public class LunarWaterBottle extends Item {

    public LunarWaterBottle() {
        super(new Item.Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        player.startUsingItem(hand);

        return InteractionResultHolder.sidedSuccess(
                player.getItemInHand(hand),
                level.isClientSide()
        );
    }

    @Override
    public ItemStack finishUsingItem(
            ItemStack stack,
            Level level,
            LivingEntity entity
    ) {
        if (!level.isClientSide()) {
            applyLunarWater.execute(entity);
        }

        if (entity instanceof Player player) {

            if (!player.isCreative()) {
                return new ItemStack(net.minecraft.world.item.Items.GLASS_BOTTLE);
            }

            // Creative players keep the Lunar Water Bottle.
            return stack;
        }

        // Non-player living entities don't normally drink this,
        // but don't leave a consumed item behind.
        return ItemStack.EMPTY;
    }

    @Override
    public ICapabilityProvider initCapabilities(
            ItemStack stack,
            @Nullable CompoundTag nbt
    ) {
        return new ICapabilityProvider() {

            private final IFluidHandlerItem fluidHandler =
                    new LunarWaterFluidHandler(stack);

            private final LazyOptional<IFluidHandlerItem> capability =
                    LazyOptional.of(() -> fluidHandler);

            @Override
            public <T> LazyOptional<T> getCapability(
                    Capability<T> cap,
                    @Nullable net.minecraft.core.Direction side
            ) {
                if (cap == ForgeCapabilities.FLUID_HANDLER_ITEM) {
                    return capability.cast();
                }

                return LazyOptional.empty();
            }
        };
    }

    private static class LunarWaterFluidHandler implements IFluidHandlerItem {

        private static final int VOLUME = FluidType.BUCKET_VOLUME / 4;

        private final ItemStack container;

        public LunarWaterFluidHandler(ItemStack container) {
            this.container = container;
        }

        @Override
        public ItemStack getContainer() {
            return container;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            if (tank != 0) {
                return FluidStack.EMPTY;
            }

            return new FluidStack(
                    ModFluids.LUNAR_WATER.get(),
                    VOLUME
            );
        }

        @Override
        public int getTankCapacity(int tank) {
            return VOLUME;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack fluid) {
            return tank == 0
                    && fluid.getFluid() == ModFluids.LUNAR_WATER.get();
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            // Lunar Water Bottle is already full.
            return 0;
        }

        @Override
        public FluidStack drain(
                FluidStack resource,
                FluidAction action
        ) {
            if (resource.isEmpty()
                    || resource.getFluid() != ModFluids.LUNAR_WATER.get()
                    || resource.getAmount() < VOLUME) {
                return FluidStack.EMPTY;
            }

            FluidStack drained = new FluidStack(
                    ModFluids.LUNAR_WATER.get(),
                    VOLUME
            );

            if (action.execute()) {
                container.shrink(1);
            }

            return drained;
        }

        @Override
        public FluidStack drain(
                int amount,
                FluidAction action
        ) {
            if (amount < VOLUME) {
                return FluidStack.EMPTY;
            }

            FluidStack drained = new FluidStack(
                    ModFluids.LUNAR_WATER.get(),
                    VOLUME
            );

            if (action.execute()) {
                container.shrink(1);
            }

            return drained;
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}