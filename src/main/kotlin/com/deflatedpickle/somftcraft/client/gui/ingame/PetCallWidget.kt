/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.client.gui.ingame

import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class PetCallWidget(
    x: Int,
    y: Int,
    action: PressAction
) : TexturedButtonWidget(
    x, y,
    16, 16,
    0, 0,
    16,
    Identifier("somftcraft", "textures/gui/whistle_button.png"),
    16, 32,
    action
) {
    init {
        tooltip = Tooltip.create(Text.translatable("gui.pet_manager.call"))
    }
}
