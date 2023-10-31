/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.screen

import com.deflatedpickle.somftcraft.Impl
import com.mojang.datafixers.util.Pair
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Equippable
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Identifier

class ArmorStandScreenHandler(
    syncId: Int,
    val player: PlayerEntity,
    val armourStand: ArmorStandEntity
) : ScreenHandler(
    null,
    syncId
) {
    val armorStandInventory = SimpleInventory(4)

    init {
        val inv = player.inventory

        for (i in 0..3) {
            val equipmentSlot = PlayerScreenHandler.EQUIPMENT_SLOT_ORDER[i]
            addSlot(object : Slot(armorStandInventory, i, 54, 8 + i * 18) {
                init {
                    stack = armourStand.getEquippedStack(equipmentSlot)
                }

                override fun setStackByPlayer(stack: ItemStack) {
                    super.setStackByPlayer(stack)

                    Equippable.get(stack)?.let {
                        armourStand.equipStack(
                            equipmentSlot,
                            stack,
                        )
                    }
                }

                override fun getMaxItemCount() = 1
                override fun canInsert(stack: ItemStack) = equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack)
                override fun canTakeItems(playerEntity: PlayerEntity) = !stack.isEmpty

                override fun getBackgroundSprite(): Pair<Identifier, Identifier> =
                    Pair.of(
                        PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
                        PlayerScreenHandler.EMPTY_ARMOR_SLOT_TEXTURES[
                            PlayerScreenHandler.EQUIPMENT_SLOT_ORDER[i].entitySlotId
                        ]
                    )
            })
        }

        for (j in 0..2) {
            for (k in 0..8) {
                addSlot(Slot(inv, k + j * 9 + 9, 8 + k * 18, j * 18 + 49 + 27 + 8))
            }
        }

        for (j in 0..8) {
            addSlot(Slot(inv, j, 8 + j * 18, 142))
        }

        Impl.addExtraSlots(this, inv)
    }

    // TODO
    override fun quickTransfer(
        player: PlayerEntity,
        fromIndex: Int
    ): ItemStack = ItemStack.EMPTY

    override fun canUse(player: PlayerEntity) = true
}
