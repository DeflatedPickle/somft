/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.deflatedpickle.somft.client.gui.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.sound.SoundEvent
import net.minecraft.text.CommonTexts
import net.minecraft.util.Identifier

open class SoundIconWidget(
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
    val soundEvent: SoundEvent,
    action: PressAction,
) : ButtonWidget(
    x, y,
    width, height,
    CommonTexts.EMPTY,
    action, DEFAULT_NARRATION
) {
    override fun drawWidget(graphics: GuiGraphics?, mouseX: Int, mouseY: Int, delta: Float) {
        drawTexture(
            graphics, this.texture,
            x, y,
            this.u, this.v, this.hoveredVOffset,
            width, height,
            this.textureWidth, this.textureHeight
        )
    }

    override fun playDownSound(soundManager: SoundManager) {
        soundManager.play(PositionedSoundInstance.master(this.soundEvent, 1.0f))
    }
}
