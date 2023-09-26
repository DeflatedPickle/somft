/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.render.entity.model;

import com.deflatedpickle.somftcraft.api.HasPieces;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings({"unused", "UnusedMixin"})
@Mixin(CompositeEntityModel.class)
public abstract class CompositeEntityModelMixin implements HasPieces {
  @Shadow
  public abstract Iterable<ModelPart> getParts();

  @NotNull
  @Override
  public List<ModelPart> somftcraft$getPieces() {
    return Lists.newLinkedList(getParts());
  }
}
