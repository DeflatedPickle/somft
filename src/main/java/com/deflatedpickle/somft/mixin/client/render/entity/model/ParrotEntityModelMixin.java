/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.render.entity.model;

import com.deflatedpickle.somft.api.GetParts;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ParrotEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"UnusedMixin", "rawtypes", "SpellCheckingInspection"})
@Mixin(ParrotEntityModel.class)
public abstract class ParrotEntityModelMixin extends SinglePartEntityModel implements GetParts {

  @Shadow
  public abstract ModelPart getPart();

  @Shadow @Final private ModelPart head;
  @Shadow @Final private ModelPart body;
  @Shadow @Final private ModelPart tail;
  @Shadow @Final private ModelPart leftWing;
  @Shadow @Final private ModelPart rightWing;
  @Shadow @Final private ModelPart leftLeg;
  @Shadow @Final private ModelPart rightLeg;
  @Unique private final boolean headScaled = false;
  @Unique private final float childHeadYOffset = 3f; // 5f
  @Unique private final float childHeadZOffset = 2f;
  @Unique private final float invertedChildHeadScale = 2f;
  @Unique private final float invertedChildBodyScale = 2f;
  @Unique private final float childBodyYOffset = 24f;

  @Override
  public void render(
      MatrixStack matrices,
      VertexConsumer vertices,
      int light,
      int overlay,
      float red,
      float green,
      float blue,
      float alpha) {
    if (this.child) {
      matrices.push();
      if (this.headScaled) {
        float f = 1.5F / this.invertedChildHeadScale;
        matrices.scale(f, f, f);
      }

      matrices.translate(0.0F, this.childHeadYOffset / 16.0F, this.childHeadZOffset / 16.0F);
      this.somft$getHeadParts()
          .forEach(
              headPart ->
                  headPart.render(matrices, vertices, light, overlay, red, green, blue, alpha));
      matrices.pop();
      matrices.push();
      float f = 1.0F / this.invertedChildBodyScale;
      matrices.scale(f, f, f);
      matrices.translate(0.0F, this.childBodyYOffset / 16.0F, 0.0F);
      this.somft$getBodyParts()
          .forEach(
              bodyPart ->
                  bodyPart.render(matrices, vertices, light, overlay, red, green, blue, alpha));
      matrices.pop();
    } else {
      this.somft$getHeadParts()
          .forEach(
              headPart ->
                  headPart.render(matrices, vertices, light, overlay, red, green, blue, alpha));
      this.somft$getBodyParts()
          .forEach(
              bodyPart ->
                  bodyPart.render(matrices, vertices, light, overlay, red, green, blue, alpha));
    }
  }

  @Redirect(
      method = "poseOnShoulder",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"))
  public void somft$poseOnShoulder$render(
      ModelPart instance, MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
    this.render(matrices, vertices, light, overlay, 1f, 1f, 1f, 1f);
  }

  @NotNull
  @Override
  public Iterable<ModelPart> somft$getHeadParts() {
    return ImmutableList.of(this.head);
  }

  @NotNull
  @Override
  public Iterable<ModelPart> somft$getBodyParts() {
    return ImmutableList.of(
        this.body, this.tail, this.leftWing, this.rightWing, this.leftLeg, this.rightLeg);
  }
}
