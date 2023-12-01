/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection")

package com.deflatedpickle.somft.config

import com.deflatedpickle.somft.api.Anchor
import com.deflatedpickle.somft.api.HotbarLayout
import net.minecraft.util.DyeColor
import org.quiltmc.config.api.Config.Section
import org.quiltmc.config.api.WrappedConfig

class SomftCraftConfig : WrappedConfig() {
    val hudConfig = HUDConfig()

    class HUDConfig : Section {
        val showControls = false
        val showLilGuy = true
        val showTools = true

        val controlsHUDConfig = ControlsHUDConfig()
        val miniMeHUDConfig = MiniMeHUDConfig()
        val hotbarConfig = HotbarConfig()

        class ControlsHUDConfig : Section {
            val colour = DyeColor.WHITE.signColor
            val pressedColour = DyeColor.LIGHT_GRAY.signColor
            val unboundColour = DyeColor.GRAY.signColor
        }

        class MiniMeHUDConfig : Section {
            val playerSize = 32
        }

        class HotbarConfig : Section {
            val layout = HotbarLayout.ROW
            val anchor = Anchor.S
            val horizontalPadding = 0f
            val verticalPadding = 10f
        }
    }
}
