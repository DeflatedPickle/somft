/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.screen

import com.mojang.datafixers.util.Pair
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.screen.PlayerScreenHandler.EQUIPMENT_SLOT_ORDER
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Identifier

class ArmorSlot(
    inventory: Inventory,
    private val owner: PlayerEntity,
    private val equipmentSlot: EquipmentSlot,
) : Slot(inventory, 39 - EQUIPMENT_SLOT_ORDER.indexOf(equipmentSlot), -12, 8 + EQUIPMENT_SLOT_ORDER.indexOf(equipmentSlot) * 18) {
    override fun setStackByPlayer(stack: ItemStack) {
        PlayerScreenHandler.onEquipItem(owner, equipmentSlot, stack, this.stack)
        super.setStackByPlayer(stack)
    }

    override fun getMaxItemCount() = 1

    override fun canInsert(stack: ItemStack?) = equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack)

    override fun canTakeItems(playerEntity: PlayerEntity): Boolean {
        val itemStack = this.stack
        return if (!itemStack.isEmpty && !playerEntity.isCreative && EnchantmentHelper.hasBindingCurse(itemStack)) false else super.canTakeItems(
            playerEntity
        )
    }

    override fun getBackgroundSprite(): Pair<Identifier, Identifier> =
        Pair.of(
            PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
            PlayerScreenHandler.EMPTY_ARMOR_SLOT_TEXTURES[equipmentSlot.entitySlotId]
        )
}
