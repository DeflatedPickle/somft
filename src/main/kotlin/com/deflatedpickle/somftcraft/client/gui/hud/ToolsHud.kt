/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.client.gui.hud

import com.deflatedpickle.somftcraft.SomftCraftClient
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.item.Items

class ToolsHud(
    val client: MinecraftClient
) {
    val padding = 5

    val playerSize = 32

    fun render(graphics: GuiGraphics) {
        if (this.client.options.debugEnabled || !SomftCraftClient.CONFIG.hudConfig.showTools) return

        val textRenderer = this.client.textRenderer
        val player = this.client.player ?: return

        val tools = mutableListOf(Items.CLOCK, Items.COMPASS, Items.RECOVERY_COMPASS)

        var x = 0
        var y = 0

        for (item in tools) {
            if (player.inventory.contains(item.defaultStack) && player.inventory.indexOf(item.defaultStack) !in 0..8) {
                graphics.drawItem(
                    item.defaultStack,
                    padding + x * 16,
                    (padding + player.height * playerSize - 8 + y * 16).toInt() + textRenderer.fontHeight * if (player.isCreative || player.hasPermissionLevel(2)) 5 else 4
                )

                if (x + 1 >= 2) {
                    x = 0
                    y++
                } else {
                    x++
                }
            }
        }
    }
}
