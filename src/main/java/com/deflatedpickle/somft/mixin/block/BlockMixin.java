/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block;

import com.deflatedpickle.somft.Somft;
import com.deflatedpickle.somft.api.DirectionalFireSpreader;
import com.deflatedpickle.somft.api.FireSpreader;
import java.util.List;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"UnusedMixin", "deprecation"})
@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
  @Shadow
  public abstract BlockState getDefaultState();

  @Shadow private BlockState defaultState;

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

    if (state.getBlock() instanceof FireSpreader fireSpreader
        && world.getGameRules().getBoolean(Somft.INSTANCE.getDO_BLOCK_FIRE_GRIEF())) {
      var fire = (FireBlock) Blocks.FIRE;

      if (fireSpreader instanceof DirectionalFireSpreader directionalFireSpreader) {
        var direction = directionalFireSpreader.somft$getDirection(state);
        var chance = fireSpreader.somft$getChance(state, direction);

        if (chance > 0) {
          fire.trySpreadingFire(world, pos.offset(direction), chance, random, 0);
        }
      } else {
        var eastChance = fireSpreader.somft$getChance(state, Direction.EAST);
        if (eastChance > 0) {
          fire.trySpreadingFire(world, pos.east(), eastChance, random, 0);
        }

        var westChance = fireSpreader.somft$getChance(state, Direction.WEST);
        if (westChance > 0) {
          fire.trySpreadingFire(world, pos.west(), westChance, random, 0);
        }

        var downChance = fireSpreader.somft$getChance(state, Direction.DOWN);
        if (downChance > 0) {
          fire.trySpreadingFire(world, pos.down(), downChance, random, 0);
        }

        var upChance = fireSpreader.somft$getChance(state, Direction.UP);
        if (upChance > 0) {
          fire.trySpreadingFire(world, pos.up(), upChance, random, 0);
        }

        var northChance = fireSpreader.somft$getChance(state, Direction.NORTH);
        if (northChance > 0) {
          fire.trySpreadingFire(world, pos.north(), northChance, random, 0);
        }

        var southChance = fireSpreader.somft$getChance(state, Direction.SOUTH);
        if (southChance > 0) {
          fire.trySpreadingFire(world, pos.south(), southChance, random, 0);
        }
      }

      world.scheduleBlockTick(pos, (Block) (Object) this, 30 + world.random.nextInt(10));
    }
  }

  @Inject(method = "appendTooltip", at = @At("HEAD"))
  public void onAppendTooltip(
      ItemStack stack,
      @Nullable BlockView world,
      List<Text> tooltip,
      TooltipContext options,
      CallbackInfo ci) {
    var block = (Block) (Object) this;
    var state = this.defaultState;

    if (this instanceof Oxidizable) {
      addTooltip(tooltip, "oxidized");
    }

    if (block == Blocks.WAXED_COPPER_BLOCK
        || block == Blocks.WAXED_WEATHERED_COPPER
        || block == Blocks.WAXED_EXPOSED_COPPER
        || block == Blocks.WAXED_OXIDIZED_COPPER
        || block == Blocks.WAXED_OXIDIZED_CUT_COPPER
        || block == Blocks.WAXED_WEATHERED_CUT_COPPER
        || block == Blocks.WAXED_EXPOSED_CUT_COPPER
        || block == Blocks.WAXED_CUT_COPPER
        || block == Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS
        || block == Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS
        || block == Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS
        || block == Blocks.WAXED_CUT_COPPER_STAIRS
        || block == Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB
        || block == Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB
        || block == Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB
        || block == Blocks.WAXED_CUT_COPPER_SLAB) {
      addTooltip(tooltip, "waxed");
    }

    if (block instanceof JukeboxBlock) {
      addTooltip(tooltip, "jukebox");
    }

    if (AxeItem.STRIPPED_BLOCKS.containsKey(this)) {
      addTooltip(tooltip, "strippable");
    }

    if (HoeItem.TILLING_ACTIONS.containsKey(this)) {
      addTooltip(tooltip, "tillable");
    }

    if (ShovelItem.PATH_STATES.containsKey(this)) {
      addTooltip(tooltip, "pathable");
    }

    if (CampfireBlock.canBeLit(state)
        || CandleBlock.canBeLit(state)
        || CandleCakeBlock.canBeLit(state)) {
      addTooltip(tooltip, "lightable");
    }
  }

  @Unique
  private void addTooltip(List<Text> tooltip, String key) {
    tooltip.add(CommonTexts.EMPTY);
    tooltip.add(Text.translatable("block.minecraft." + key + ".desc1").formatted(Formatting.GRAY));
    tooltip.add(
        CommonTexts.space()
            .append(
                Text.translatable("block.minecraft." + key + ".desc2").formatted(Formatting.BLUE)));
  }
}
