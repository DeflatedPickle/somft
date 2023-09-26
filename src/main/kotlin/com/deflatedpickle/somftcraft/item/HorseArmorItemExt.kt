/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.somftcraft.item

import net.minecraft.item.HorseArmorItem
import net.minecraft.util.Identifier

class HorseArmorItemExt(
    bonus: Int,
    name: String,
    settings: Settings,
) : HorseArmorItem(bonus, name, settings) {
    override fun getEntityTexture(): Identifier {
        return Identifier("somftcraft", entityTexture)
    }
}