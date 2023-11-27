/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.client.gui.widget

import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

class MultiStateButton<T>(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    val values: List<T>,
    var selectedIndex: Int = 0,
    pressWidget: PressAction,
) : ButtonWidget(
    x, y,
    width, height,
    Text.of(values.first().toString()),
    pressWidget,
    DEFAULT_NARRATION
) {
    override fun onClick(mouseX: Double, mouseY: Double) {
        super.onClick(mouseX, mouseY)
        if (selectedIndex + 1 == values.size) {
            selectedIndex = 0
        } else {
            selectedIndex++
        }
        message = Text.of(values[selectedIndex].toString())
    }
}
