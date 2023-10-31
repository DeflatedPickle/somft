/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.screen

import com.mojang.datafixers.util.Pair
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Identifier

class OffHandSlot(
    inventory: Inventory,
    private val owner: PlayerEntity,
) : Slot(inventory, 40, -12, 8 + 4 * 18) {
    override fun setStackByPlayer(stack: ItemStack) {
        PlayerScreenHandler.onEquipItem(owner, EquipmentSlot.OFFHAND, stack, stack)
        super.setStackByPlayer(stack)
    }

    override fun getBackgroundSprite(): Pair<Identifier, Identifier> =
        Pair.of(
            PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
            PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT
        )
}
