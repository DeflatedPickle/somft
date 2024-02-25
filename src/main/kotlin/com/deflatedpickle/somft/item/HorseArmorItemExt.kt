/* Copyright (c) 2023-2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.item

import net.minecraft.item.HorseArmorItem
import net.minecraft.util.Identifier

class HorseArmorItemExt(
    bonus: Int,
    name: String,
    settings: Settings,
) : HorseArmorItem(bonus, name, settings) {
    override fun getEntityTexture(): Identifier {
        return Identifier("somft", entityTexture)
    }
}
