/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.client.gui.ingame

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.util.Identifier

class PetButtonWidget(
    x: Int,
    y: Int,
) : TexturedButtonWidget(
    x, y,
    16, 16,
    0, 0,
    16,
    Identifier("somftcraft", "textures/gui/pet_manager_button.png"),
    16, 32,
    {
        MinecraftClient.getInstance().let { client ->
            client.player?.let { player ->
                client.setScreen(
                    PetManagerScreen(player)
                )
            }
        }
    }
)
