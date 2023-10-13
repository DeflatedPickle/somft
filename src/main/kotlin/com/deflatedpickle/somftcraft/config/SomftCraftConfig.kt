/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("SpellCheckingInspection")

package com.deflatedpickle.somftcraft.config

import org.quiltmc.config.api.Config.Section
import org.quiltmc.config.api.WrappedConfig

class SomftCraftConfig : WrappedConfig() {
    val hudConfig = HUDConfig()

    class HUDConfig : Section {
        val showControls = true
        val showLilGuy = true
        val showTools = true
    }
}
