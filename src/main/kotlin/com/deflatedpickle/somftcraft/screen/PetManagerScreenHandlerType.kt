/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.screen

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.feature_flags.FeatureFlags
import net.minecraft.screen.ScreenHandlerType

object PetManagerScreenHandlerType : ScreenHandlerType<PetManagerScreenHandler>(
    { i: Int, playerInventory: PlayerInventory ->
        PetManagerScreenHandler(i, playerInventory)
    },
    FeatureFlags.DEFAULT_SET
)
