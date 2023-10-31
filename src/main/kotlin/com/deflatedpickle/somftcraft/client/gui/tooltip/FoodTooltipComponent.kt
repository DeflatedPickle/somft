/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.deflatedpickle.somftcraft.client.gui.tooltip

import com.deflatedpickle.somftcraft.client.item.FoodTooltipData
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.util.Identifier

class FoodTooltipComponent(
    val foodData: FoodTooltipData,
) : TooltipComponent {
    companion object {
        val ICONS = Identifier("textures/gui/icons.png")
    }

    override fun getHeight() = 9

    override fun getWidth(textRenderer: TextRenderer) = 10 * 9

    override fun drawItems(
        textRenderer: TextRenderer,
        x: Int,
        y: Int,
        graphics: GuiGraphics
    ) {
        val k = foodData.foodComponent.hunger
        for (s in 0..9) {
            val x = x + 10 * 8 - s * 8 - 9
            graphics.drawTexture(
                ICONS,
                x, y,
                16, 27,
                9, 9
            )

            if (s * 2 + 1 < k) {
                graphics.drawTexture(
                    ICONS,
                    x, y,
                    16 + 36, 27,
                    9, 9
                )
            }

            if (s * 2 + 1 == k) {
                graphics.drawTexture(
                    ICONS,
                    x, y,
                    16 + 45, 27,
                    9, 9
                )
            }
        }
    }
}
