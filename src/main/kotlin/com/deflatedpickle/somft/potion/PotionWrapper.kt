/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.potion

import net.minecraft.potion.Potion

data class PotionWrapper(
    val potion: Potion
) : Comparable<PotionWrapper> {
    override fun compareTo(other: PotionWrapper) =
        if (this.potion == other.potion) 0
        else -1
}
