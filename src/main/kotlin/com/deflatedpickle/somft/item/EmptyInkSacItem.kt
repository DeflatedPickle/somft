/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.item

import net.minecraft.advancement.criterion.Criteria
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.GlowSquidEntity
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.event.GameEvent
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

object EmptyInkSacItem : Item(QuiltItemSettings()) {
    override fun useOnEntity(
        stack: ItemStack,
        user: PlayerEntity,
        entity: LivingEntity,
        hand: Hand
    ): ActionResult {
        with(entity.world) {
            playSound(user, user.x, user.y, user.z, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0f, 1.0f)
            emitGameEvent(user, GameEvent.FLUID_PICKUP, entity.blockPos)
        }

        if (user is ServerPlayerEntity) {
            Criteria.PLAYER_INTERACTED_WITH_ENTITY.trigger(user, stack, entity)
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this))

        ItemUsage.exchangeStack(
            stack,
            user,
            ItemStack(
                when (entity) {
                    is GlowSquidEntity -> Items.GLOW_INK_SAC
                    is SquidEntity -> Items.INK_SAC
                    else -> TODO()
                }
            )
        )

        return ActionResult.success(entity.world.isClient)
    }
}
