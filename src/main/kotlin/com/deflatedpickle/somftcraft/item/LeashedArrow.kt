/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("KotlinConstantConditions", "MemberVisibilityCanBePrivate")

package com.deflatedpickle.somftcraft.item

import com.deflatedpickle.somftcraft.api.Leashable
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.resource.language.I18n
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.SpectralArrowEntity
import net.minecraft.item.ArrowItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.SpectralArrowItem
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Rarity
import net.minecraft.world.World
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

object LeashedArrow : ArrowItem(QuiltItemSettings()) {
    // TODO: make a custom model to display the original arrow

    override fun getName(stack: ItemStack): MutableText =
        Text.literal(I18n.translate("item.somftcraft.leashed_arrow", getArrowStack(stack).name.string))

    override fun getRarity(stack: ItemStack): Rarity = getArrowStack(stack).rarity

    fun setArrowStack(stack: ItemStack, other: ItemStack): ItemStack {
        val nbt = stack.getOrCreateSubNbt("Arrow")
        other.writeNbt(nbt)
        return stack
    }

    fun getArrowStack(stack: ItemStack): ItemStack =
        ItemStack.fromNbt(stack.getOrCreateSubNbt("Arrow"))

    override fun getDefaultStack() =
        setArrowStack(ItemStack(this), Items.ARROW.defaultStack)

    override fun createArrow(
        world: World,
        stack: ItemStack,
        shooter: LivingEntity
    ): PersistentProjectileEntity {
        val arrow = getArrowStack(stack)
        val arrowEntity = when (arrow.item) {
            is SpectralArrowItem -> SpectralArrowEntity(world, shooter)
            else -> ArrowEntity(world, shooter).apply { initFromStack(arrow) }
        }
        (arrowEntity as Leashable).`somft$attachLeash`(shooter, true)
        return arrowEntity
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        val arrow = getArrowStack(stack)
        // tooltip.add(arrow.name)
        arrow.item.appendTooltip(arrow, world, tooltip, context)
    }
}
