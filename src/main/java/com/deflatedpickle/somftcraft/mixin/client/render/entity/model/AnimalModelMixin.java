/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.render.entity.model;

import com.deflatedpickle.somftcraft.api.HasPieces;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("UnusedMixin")
@Mixin(AnimalModel.class)
public abstract class AnimalModelMixin implements HasPieces {
  @Shadow
  public abstract Iterable<ModelPart> getHeadParts();

  @Shadow
  public abstract Iterable<ModelPart> getBodyParts();

  @NotNull
  @Override
  public List<ModelPart> somftcraft$getPieces() {
    return Stream.of(Lists.newLinkedList(getHeadParts()), Lists.newLinkedList(getBodyParts()))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }
}
