/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.client.gui.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.widget.CheckboxWidget
import net.minecraft.text.CommonTexts
import net.minecraft.util.Identifier

open class TexturedCheckboxWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    checked: Boolean,
    val u: Int,
    val v: Int,
    val hoveredVOffset: Int,
    val identifier: Identifier,
    val textureWidth: Int,
    val textureHeight: Int,
) : CheckboxWidget(
    x, y, width, height, CommonTexts.EMPTY, checked
) {
    override fun drawWidget(graphics: GuiGraphics?, mouseX: Int, mouseY: Int, delta: Float) {
        drawTexture(
            graphics, this.identifier,
            x, y,
            this.u, if (isChecked) this.hoveredVOffset else 0,
            this.hoveredVOffset,
            width, height,
            this.textureWidth, this.textureHeight
        )
    }
}
