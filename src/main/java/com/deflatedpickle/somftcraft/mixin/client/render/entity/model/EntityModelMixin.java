/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.render.entity.model;

import com.deflatedpickle.somftcraft.api.HasPieces;
import java.util.List;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"unused", "UnusedMixin"})
@Mixin(EntityModel.class)
public abstract class EntityModelMixin implements HasPieces {
  @NotNull
  @Override
  public List<ModelPart> somftcraft$getPieces() {
    return List.of();
  }
}
