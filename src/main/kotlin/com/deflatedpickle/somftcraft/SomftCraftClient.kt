/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("unused", "SpellCheckingInspection", "HasPlatformType")

package com.deflatedpickle.somftcraft

import com.deflatedpickle.somftcraft.config.SomftCraftConfig
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.config.QuiltConfig
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer

object SomftCraftClient : ClientModInitializer {
    val CONFIG = QuiltConfig.create("somftcraft", "config", SomftCraftConfig::class.java)

    override fun onInitializeClient(mod: ModContainer) {
        CONFIG
    }
}
