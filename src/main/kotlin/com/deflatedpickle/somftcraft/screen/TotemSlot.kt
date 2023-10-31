/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.screen

import com.mojang.datafixers.util.Pair
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Identifier

class TotemSlot(
    inventory: Inventory,
    private val owner: PlayerEntity,
) : Slot(inventory, TOTEM_SLOT, 77, 62 - 18) {
    companion object {
        val EMPTY_TOTEM_SLOT = Identifier("somftcraft", "item/empty_totem_slot")
        val TOTEM_SLOT = PlayerInventory.OFF_HAND_SLOT + 1
    }

    override fun canInsert(stack: ItemStack) = false // stack.item == Items.TOTEM_OF_UNDYING

    override fun getBackgroundSprite(): Pair<Identifier, Identifier> =
        Pair.of(
            PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
            EMPTY_TOTEM_SLOT
        )
}
