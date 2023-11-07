/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.deflatedpickle.somft.client.gui.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.CommonTexts
import net.minecraft.util.Identifier

class IconButtonWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    val u: Int,
    val v: Int,
    val hoveredVOffset: Int,
    val texture: Identifier,
    val textureWidth: Int,
    val textureHeight: Int,
    pressWidget: PressAction,
) : ButtonWidget(
    x, y,
    width, height,
    CommonTexts.EMPTY,
    pressWidget,
    DEFAULT_NARRATION
) {
    public override fun drawWidget(
        graphics: GuiGraphics?,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        super.drawWidget(graphics, mouseX, mouseY, delta)
        drawTexture(
            graphics, this.texture,
            x, y,
            this.u, this.v, this.hoveredVOffset,
            width, height,
            this.textureWidth, this.textureHeight
        )
    }
}
