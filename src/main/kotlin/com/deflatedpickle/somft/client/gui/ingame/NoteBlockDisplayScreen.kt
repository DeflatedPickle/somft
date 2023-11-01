/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.client.gui.ingame

import net.minecraft.block.NoteBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class NoteBlockDisplayScreen(
    val world: World,
    val blockPos: BlockPos,
) : Screen(
    Text.translatable("gui.noteblock")
) {
    companion object {
        val client = MinecraftClient.getInstance()
        val textRenderer = client.textRenderer

        val TEXTURE = Identifier("somftcraft", "textures/gui/keys.png")

        val sharpsNFlats = listOf("F#", "G#", "A#", null, "C#", "D#", null, "F#", "G#", "A#", null, "C#", "D#", null, "F#")
        val natural = listOf("G", "A", "B", "C", "D", "E", "F", "G", "A", "B", "C", "D", "E", "F")

        const val noteWidth = 32
        const val naturalNoteHeight = 160
        const val sharpsNFlatsNoteHeight = 106
    }

    override fun init() {
        super.init()

        for (l in listOf(natural, sharpsNFlats)) {
            for ((i, v) in l.reversed().withIndex()) {
                if (v != null) {
                    addDrawableChild(
                        NoteButton(
                            (this.width / 2 - noteWidth / 2) - (i * noteWidth) + (natural.size / 2 * noteWidth) - if (l.contains(null)) 0 else noteWidth / 2,
                            this.height / 2 - naturalNoteHeight / 2,
                            v,
                            !l.contains(null)
                        ) {
                            // TODO: send the server a packet to change the note
                        }
                    )
                }
            }
        }
    }

    override fun render(
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        this.renderBackground(graphics)
        super.render(graphics, mouseX, mouseY, delta)
    }

    override fun renderBackground(graphics: GuiGraphics) {
        super.renderBackground(graphics)

        val state = world.getBlockState(blockPos)

        graphics.drawCenteredShadowedText(
            MinecraftClient.getInstance().textRenderer,
            state.get(NoteBlock.INSTRUMENT).toString(),
            this.width / 2,
            this.height / 2 - naturalNoteHeight / 2 - MinecraftClient.getInstance().textRenderer.fontHeight * 3,
            DyeColor.WHITE.signColor
        )

        graphics.drawCenteredShadowedText(
            MinecraftClient.getInstance().textRenderer,
            state.get(NoteBlock.NOTE).toString(),
            this.width / 2,
            this.height / 2 - naturalNoteHeight / 2 - MinecraftClient.getInstance().textRenderer.fontHeight * 2,
            DyeColor.WHITE.signColor
        )
    }

    class NoteButton(
        x: Int,
        y: Int,
        val text: String,
        val neutral: Boolean,
        pressAction: PressAction,
    ) : TexturedButtonWidget(
        x, y,
        noteWidth, if (neutral) naturalNoteHeight else sharpsNFlatsNoteHeight,
        if (neutral) 0 else noteWidth, 0, 0,
        TEXTURE,
        64, 160,
        pressAction
    ) {
        override fun drawWidget(
            graphics: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            delta: Float
        ) {
            super.drawWidget(graphics, mouseX, mouseY, delta)
            graphics.drawText(
                textRenderer,
                Text.literal(text),
                x + width / 2 - textRenderer.getWidth(text) / 2, y + height - textRenderer.fontHeight - 4,
                (if (neutral) DyeColor.BLACK else DyeColor.WHITE).signColor,
                false
            )
        }
    }
}
