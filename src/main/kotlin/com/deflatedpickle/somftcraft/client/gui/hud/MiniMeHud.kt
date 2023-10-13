/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.client.gui.hud

import com.deflatedpickle.somftcraft.SomftCraftClient
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.ingame.InventoryScreen

class MiniMeHud(
    val client: MinecraftClient
) {
    val padding = 5

    val playerSize = 32

    fun render(graphics: GuiGraphics) {
        if (this.client.options.debugEnabled || !SomftCraftClient.CONFIG.hudConfig.showLilGuy) return

        val textRenderer = this.client.textRenderer
        val player = this.client.player ?: return

        InventoryScreen.drawEntity(
            graphics,
            (padding + player.width * playerSize).toInt(),
            (padding + player.height * playerSize).toInt() + textRenderer.fontHeight * if (player.isCreative || player.hasPermissionLevel(2)) 5 else 4,
            playerSize,
            -graphics.scaledWindowWidth / 2f,
            player.renderPitch,
            player
        )
    }
}
