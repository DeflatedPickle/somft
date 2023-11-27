/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.screen

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot

class PetManagerScreenHandler(
    syncId: Int,
    val playerInventory: PlayerInventory,
) : ScreenHandler(
    PetManagerScreenHandlerType,
    syncId
) {
    init {
        for (j in 0..2) {
            for (k in 0..8) {
                addSlot(Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, j * 18 + 49 + 27 + 8))
            }
        }

        for (j in 0..8) {
            addSlot(Slot(playerInventory, j, 8 + j * 18, 142))
        }
    }

    override fun quickTransfer(
        player: PlayerEntity,
        fromIndex: Int
    ): ItemStack = ItemStack.EMPTY

    override fun canUse(player: PlayerEntity) = true
}
