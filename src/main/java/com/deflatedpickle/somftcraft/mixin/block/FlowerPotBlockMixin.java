/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"deprecation", "UnusedMixin"})
@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin extends Block {
  public FlowerPotBlockMixin(Settings settings) {
    super(settings);
  }

  @Override
  public void onProjectileHit(
      World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
    BlockPos blockPos = hit.getBlockPos();
    if (!world.isClient
        && projectile.canModifyAt(world, blockPos)
        && projectile.getType().isIn(EntityTypeTags.IMPACT_PROJECTILES)) {
      world.breakBlock(blockPos, true, projectile);
    }
  }
}
