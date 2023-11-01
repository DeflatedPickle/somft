/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block.dispenser;

import com.deflatedpickle.somft.Impl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.dispenser.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(ItemDispenserBehavior.class)
public abstract class ItemDispenserBehaviorMixin {
  @Shadow
  protected abstract ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack);

  @Inject(method = "dispenseSilently", at = @At("HEAD"), cancellable = true)
  public void onDispenseSilently(
      BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
    World world = pointer.getWorld();
    BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
    BlockState blockState = world.getBlockState(blockPos);
    Block block = blockState.getBlock();
    Item item = stack.getItem();

    if (stack.isEmpty()) {
      cir.setReturnValue(this.dispenseSilently(pointer, stack));
    }

    Impl.INSTANCE.dispenseExtra(world, blockPos, blockState, block, stack, item, cir);
  }
}
