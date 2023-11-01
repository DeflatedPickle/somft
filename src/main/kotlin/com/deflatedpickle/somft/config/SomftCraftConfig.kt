/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection")

package com.deflatedpickle.somft.config

import net.minecraft.util.DyeColor
import org.quiltmc.config.api.Config.Section
import org.quiltmc.config.api.WrappedConfig

class SomftCraftConfig : WrappedConfig() {
    val hudConfig = HUDConfig()

    class HUDConfig : Section {
        val showControls = true
        val showLilGuy = true
        val showTools = true

        val controlsHUDConfig = ControlsHUDConfig()
        val miniMeHUDConfig = MiniMeHUDConfig()

        class ControlsHUDConfig : Section {
            val colour = DyeColor.WHITE.signColor
            val pressedColour = DyeColor.LIGHT_GRAY.signColor
            val unboundColour = DyeColor.GRAY.signColor
        }

        class MiniMeHUDConfig : Section {
            val playerSize = 32
        }
    }
}
