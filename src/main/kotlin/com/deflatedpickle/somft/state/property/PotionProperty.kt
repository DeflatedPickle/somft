/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.state.property

import com.deflatedpickle.somft.potion.PotionWrapper
import net.minecraft.potion.Potion
import net.minecraft.registry.Registries
import net.minecraft.state.property.Property
import java.util.Optional

class PotionProperty(
    name: String,
) : Property<PotionWrapper>(
    name, PotionWrapper::class.java
) {
    override fun getValues(): MutableCollection<PotionWrapper> =
        Registries.POTION.map {
            PotionWrapper(it)
        }.toMutableList()

    override fun parse(name: String) =
        Optional.of(PotionWrapper(Potion.byId(name)))

    override fun name(value: PotionWrapper) =
        Registries.POTION.getId(value.potion)
            .toString()
}
