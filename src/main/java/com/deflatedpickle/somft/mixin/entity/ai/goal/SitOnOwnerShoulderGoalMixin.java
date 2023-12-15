/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.ai.goal;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.Objects;
import net.minecraft.entity.ai.goal.SitOnOwnerShoulderGoal;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(SitOnOwnerShoulderGoal.class)
public abstract class SitOnOwnerShoulderGoalMixin {
  @Shadow @Final private TameableShoulderEntity tameable;

  @Shadow private ServerPlayerEntity owner;

  @Shadow private boolean mounted;

  @ModifyReturnValue(method = "canStart", at = @At("RETURN"))
  public boolean somft$canStart(boolean original) {
    return original && !this.tameable.isInLove();
  }

  @Redirect(
      method = "start",
      at =
          @At(
              value = "FIELD",
              target = "Lnet/minecraft/entity/ai/goal/SitOnOwnerShoulderGoal;mounted:Z",
              opcode = Opcodes.PUTFIELD))
  public void somft$start$mounted(SitOnOwnerShoulderGoal instance, boolean value) {
    this.mounted = isMounted();
  }

  @Inject(method = "tick", at = @At("HEAD"))
  public void somft$tick(CallbackInfo ci) {
    if (isMounted() && !this.mounted) {
      this.mounted = true;
    }
  }

  @Unique
  private boolean isMounted() {
    return Objects.equals(
            this.owner.getShoulderEntityLeft().getString("id"), this.tameable.getSavedEntityId())
        || Objects.equals(
            this.owner.getShoulderEntityRight().getString("id"), this.tameable.getSavedEntityId());
  }
}
