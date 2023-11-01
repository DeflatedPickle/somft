/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection")

package com.deflatedpickle.somft.client.tooltip

import net.minecraft.client.gui.tooltip.TooltipPositioner
import org.joml.Vector2i
import org.joml.Vector2ic

object CentredTooltipPositioner : TooltipPositioner {
    override fun position(
        screenWidth: Int,
        screenHeight: Int,
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): Vector2ic = Vector2i(x - width / 2, y)
}
