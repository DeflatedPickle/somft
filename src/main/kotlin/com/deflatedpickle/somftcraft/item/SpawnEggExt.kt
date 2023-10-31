/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.item

import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.SpawnEggItem
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

class SpawnEggExt<T : MobEntity>(
    entityType: EntityType<T>,
    primaryColor: Int,
    secondaryColor: Int,
) : SpawnEggItem(
    entityType,
    primaryColor,
    secondaryColor,
    QuiltItemSettings()
) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val player = context.player
        if (world !is ServerWorld) return ActionResult.SUCCESS
        if (player == null) return ActionResult.FAIL

        val stack = context.stack
        val nbt = stack.getOrCreateSubNbt("data")
        nbt.putUuid("owner", player.uuid)
        nbt.putBoolean("tamed", true)
        return super.useOnBlock(context)
    }
    override fun use(
        world: World,
        user: PlayerEntity,
        hand: Hand
    ): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val nbt = stack.getOrCreateSubNbt("data")
        nbt.putUuid("owner", user.uuid)
        nbt.putBoolean("tamed", true)
        return super.use(world, user, hand)
    }
}
