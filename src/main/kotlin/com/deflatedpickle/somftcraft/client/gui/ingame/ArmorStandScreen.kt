/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.client.gui.ingame

import com.deflatedpickle.somftcraft.screen.ArmorStandScreenHandler
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier

// TODO: add a quick unequip button
class ArmorStandScreen(
    handler: ArmorStandScreenHandler,
    val player: PlayerEntity,
    private val armourStand: ArmorStandEntity
) : HandledScreen<ArmorStandScreenHandler>(
    handler,
    player.inventory,
    Text.translatable("gui.armor_stand")
) {
    companion object {
        val BACKGROUND_TEXTURE = Identifier("somftcraft", "textures/gui/container/armor_stand.png")
    }

    override fun drawBackground(
        graphics: GuiGraphics,
        delta: Float,
        mouseX: Int,
        mouseY: Int
    ) {
        graphics.drawTexture(
            BACKGROUND_TEXTURE,
            x, y, 0,
            0f, 0f,
            backgroundWidth, backgroundHeight,
            backgroundWidth, backgroundHeight,
        )

        InventoryScreen.drawEntity(
            graphics,
            x + 72 + 49 / 2,
            y + 70,
            30,
            (x + 72 + 49 / 2).toFloat() - mouseX,
            (y + 70 - 50).toFloat() - mouseY,
            armourStand
        )
    }

    override fun render(
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        super.render(graphics, mouseX, mouseY, delta)
        this.drawMouseoverTooltip(graphics, mouseX, mouseY)
    }
}
