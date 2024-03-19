/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block;

import com.deflatedpickle.somft.api.DispenserExt;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.dispenser.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"SpellCheckingInspection", "UnusedMixin"})
@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin extends BlockWithEntity {
  protected DispenserBlockMixin(Settings settings) {
    super(settings);
  }

  @Inject(method = "onPlaced", at = @At("TAIL"))
  public void somft$onPlaced(
      World world,
      BlockPos pos,
      BlockState state,
      LivingEntity placer,
      ItemStack itemStack,
      CallbackInfo ci) {
    BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof DispenserBlockEntity dispenserBlockEntity) {
      var nbt = itemStack.getNbt();
      if (nbt != null && nbt.contains("Bow")) {
        ((DispenserExt) dispenserBlockEntity)
            .setSomft$bowStack(ItemStack.fromNbt(itemStack.getSubNbt("Bow")));
      }
    }
  }

  @Override
  public List<ItemStack> getDroppedStacks(
      BlockState state, LootContextParameterSet.Builder lootParameterBuilder) {
    var stack = super.getDroppedStacks(state, lootParameterBuilder).stream().findFirst().get();
    var compound = stack.getOrCreateSubNbt("Bow");

    var blockEntity = lootParameterBuilder.getParameter(LootContextParameters.BLOCK_ENTITY);

    if (blockEntity instanceof DispenserBlockEntity dispenserBlockEntity
        && dispenserBlockEntity instanceof DispenserExt dispenserExt) {
      Objects.requireNonNull(dispenserExt.getSomft$bowStack()).writeNbt(compound);
    }

    return List.of(stack);
  }
}
