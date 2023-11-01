/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.client.gui.ingame

import com.deflatedpickle.somft.client.gui.widget.TexturedCheckboxWidget
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class PetStayWidget(
    x: Int,
    y: Int,
    checked: Boolean
) : TexturedCheckboxWidget(
    x, y,
    16, 16,
    checked,
    0, 0,
    16,
    Identifier("somftcraft", "textures/gui/stay_button.png"),
    16, 32,
) {
    init {
        tooltip = Tooltip.create(Text.translatable("gui.pet_manager.stay"))
    }
}
