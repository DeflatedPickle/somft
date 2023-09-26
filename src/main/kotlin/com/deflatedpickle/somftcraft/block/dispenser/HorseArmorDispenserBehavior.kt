/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.block.dispenser

import net.minecraft.block.dispenser.DispenserBlock
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior
import net.minecraft.entity.passive.HorseBaseEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPointer
import net.minecraft.util.math.Box

object HorseArmorDispenserBehavior : FallibleItemDispenserBehavior() {
    override fun dispenseSilently(pointer: BlockPointer, stack: ItemStack): ItemStack {
        val blockPos = pointer.pos.offset(pointer.blockState.get(DispenserBlock.FACING))
        for (
            horseBaseEntity in pointer.world
                .getEntitiesByClass(
                    HorseBaseEntity::class.java, Box(blockPos)
                ) { entity: HorseBaseEntity -> entity.isAlive && entity.hasArmorSlot() }
        ) {
            if (horseBaseEntity.isHorseArmor(stack) && !horseBaseEntity.hasArmorInSlot() && horseBaseEntity.isTame) {
                horseBaseEntity.getStackReference(401).set(stack.split(1))
                this.isSuccess = true

                return stack
            }
        }
        return super.dispenseSilently(pointer, stack)
    }
}
