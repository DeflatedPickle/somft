/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.deflatedpickle.somft.client.gui.tooltip

import com.deflatedpickle.somft.Impl
import com.deflatedpickle.somft.api.IconSet.HUNGER
import com.deflatedpickle.somft.client.item.FoodTooltipData
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.tooltip.TooltipComponent

class FoodTooltipComponent(
    val foodData: FoodTooltipData,
) : TooltipComponent {
    override fun getHeight() = 9

    override fun getWidth(textRenderer: TextRenderer) = 10 * 9

    override fun drawItems(
        textRenderer: TextRenderer,
        x: Int,
        y: Int,
        graphics: GuiGraphics
    ) {
        Impl.drawIconRow(
            graphics,
            textRenderer,
            foodData.foodComponent.hunger,
            x, y,
            HUNGER
        )
    }
}
