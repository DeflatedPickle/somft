/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.render.entity;

import com.deflatedpickle.somftcraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"unused", "UnusedMixin", "rawtypes", "unchecked", "ConstantConditions"})
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
  @Shadow
  protected abstract boolean addFeature(FeatureRenderer feature);

  @Inject(method = "<init>", at = @At("TAIL"))
  public void init(
      EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius, CallbackInfo ci) {
    if ((Object) this instanceof PlayerEntityRenderer) return;

    addFeature(
        new StuckArrowsFeatureRenderer<>(
            ctx, (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) (Object) this));
  }
}
