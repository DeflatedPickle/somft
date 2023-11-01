/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block;

import com.deflatedpickle.somft.client.gui.ingame.NoteBlockDisplayScreen;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
  @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
  public void onUse(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit,
      CallbackInfoReturnable<ActionResult> cir) {
    // TODO: highlight the currently selected note
    if (player.isSneaking()) {
      if (world.isClient) {
        MinecraftClient.getInstance().setScreen(new NoteBlockDisplayScreen(world, pos));
      }
      cir.setReturnValue(ActionResult.success(world.isClient));
    }
  }
}
