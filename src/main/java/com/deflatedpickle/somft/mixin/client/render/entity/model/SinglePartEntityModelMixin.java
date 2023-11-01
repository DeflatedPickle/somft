/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.client.render.entity.model;

import com.deflatedpickle.somft.api.HasPieces;
import java.util.List;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings({"unused", "UnusedMixin"})
@Mixin(SinglePartEntityModel.class)
public abstract class SinglePartEntityModelMixin implements HasPieces {
  @Shadow
  public abstract ModelPart getPart();

  @NotNull
  @Override
  public List<ModelPart> somft$getPieces() {
    return List.of(getPart());
  }
}
