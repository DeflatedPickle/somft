/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.block;

import com.deflatedpickle.somftcraft.api.DirectionalFireSpreader;
import com.deflatedpickle.somftcraft.api.FireSpreader;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"UnusedMixin", "deprecation"})
@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
  public BlockMixin(Settings settings) {
    super(settings);
  }

  @Override
  public void onBlockAdded(
      BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
    super.onBlockAdded(state, world, pos, oldState, notify);

    if (state.getBlock() instanceof FireSpreader) {
      world.scheduleBlockTick(pos, (Block) (Object) this, 30 + world.random.nextInt(10));
    }
  }

  @Override
  public void scheduledTick(
      BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
    super.scheduledTick(state, world, pos, random);

    if (state.getBlock() instanceof FireSpreader fireSpreader) {
      var fire = (FireBlock) Blocks.FIRE;

      if (fireSpreader instanceof DirectionalFireSpreader directionalFireSpreader) {
        var direction = directionalFireSpreader.somftcraft$getDirection(state);
        var chance = fireSpreader.somftcraft$getChance(state, direction);

        if (chance > 0) {
          fire.trySpreadingFire(world, pos.offset(direction), chance, random, 0);
        }
      } else {
        var eastChance = fireSpreader.somftcraft$getChance(state, Direction.EAST);
        if (eastChance > 0) {
          fire.trySpreadingFire(world, pos.east(), eastChance, random, 0);
        }

        var westChance = fireSpreader.somftcraft$getChance(state, Direction.WEST);
        if (westChance > 0) {
          fire.trySpreadingFire(world, pos.west(), westChance, random, 0);
        }

        var downChance = fireSpreader.somftcraft$getChance(state, Direction.DOWN);
        if (downChance > 0) {
          fire.trySpreadingFire(world, pos.down(), downChance, random, 0);
        }

        var upChance = fireSpreader.somftcraft$getChance(state, Direction.UP);
        if (upChance > 0) {
          fire.trySpreadingFire(world, pos.up(), upChance, random, 0);
        }

        var northChance = fireSpreader.somftcraft$getChance(state, Direction.NORTH);
        if (northChance > 0) {
          fire.trySpreadingFire(world, pos.north(), northChance, random, 0);
        }

        var southChance = fireSpreader.somftcraft$getChance(state, Direction.SOUTH);
        if (southChance > 0) {
          fire.trySpreadingFire(world, pos.south(), southChance, random, 0);
        }
      }

      world.scheduleBlockTick(pos, (Block) (Object) this, 30 + world.random.nextInt(10));
    }
  }
}
