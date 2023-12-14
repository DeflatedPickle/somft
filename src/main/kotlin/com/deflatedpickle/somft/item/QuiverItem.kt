/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.deflatedpickle.somft.item

import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketItem
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.client.item.BundleTooltipData
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.BundleItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.tag.ItemTags
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.ClickType
import net.minecraft.util.DyeColor
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings
import java.util.Optional

// TODO: add a way to select an arrow
object QuiverItem : BundleItem(QuiltItemSettings().maxCount(1)), Trinket {
    init {
        TrinketsApi.registerTrinket(this, this)
    }

    override fun use(
        world: World,
        user: PlayerEntity,
        hand: Hand
    ): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        return if (TrinketItem.equipItem(user, stack)) {
            TypedActionResult.success(stack, world.isClient())
        } else super.use(world, user, hand)
    }

    override fun onClickedOnOther(
        thisStack: ItemStack,
        otherSlot: Slot,
        clickType: ClickType,
        player: PlayerEntity
    ) = if (clickType != ClickType.RIGHT) {
        false
    } else {
        val itemStack = otherSlot.stack
        if (itemStack.isEmpty) {
            this.playRemoveOneSound(player)
            removeFirstStack(thisStack).ifPresent { removedStack: ItemStack ->
                this.addToQuiver(
                    thisStack,
                    otherSlot.insertStack(removedStack)
                )
            }
        } else if (itemStack.isIn(ItemTags.ARROWS)) {
            val i = this.getQuiverOccupancy(thisStack)
            val j = this.addToQuiver(thisStack, otherSlot.takeStackRange(itemStack.count, i, player))
            if (j > 0) {
                this.playInsertSound(player)
            }
        }
        true
    }

    override fun onClicked(
        thisStack: ItemStack,
        otherStack: ItemStack,
        thisSlot: Slot,
        clickType: ClickType,
        player: PlayerEntity,
        cursorStackReference: StackReference
    ) = if (clickType == ClickType.RIGHT && thisSlot.canTakePartial(player)) {
        if (otherStack.isEmpty) {
            removeFirstStack(thisStack).ifPresent { itemStack: ItemStack? ->
                this.playRemoveOneSound(player)
                cursorStackReference.set(itemStack)
            }
        } else {
            val i = this.addToQuiver(thisStack, otherStack)
            if (i > 0) {
                this.playInsertSound(player)
                otherStack.decrement(i)
            }
        }
        true
    } else {
        false
    }

    override fun isItemBarVisible(stack: ItemStack) = true

    override fun getItemBarStep(stack: ItemStack) =
        if (getQuiverOccupancy(stack) <= 0) 0
        else (1 + 12 * getQuiverOccupancy(stack) / 64)
            .coerceAtMost(13)

    override fun getItemBarColor(stack: ItemStack): Int {
        return when {
            getQuiverOccupancy(stack) >= 64 -> DyeColor.RED
            getQuiverOccupancy(stack) >= 32 -> DyeColor.YELLOW
            else -> DyeColor.GREEN
        }.signColor
    }

    fun getQuiver(livingEntity: LivingEntity): Optional<ItemStack> {
        val optional = TrinketsApi.getTrinketComponent(livingEntity)
        if (optional.isPresent) {
            val trinket = optional.get()
            for (entry in trinket.getEquipped(QuiverItem)) {
                return Optional.of(entry.right)
            }
        }
        return Optional.empty()
    }

    fun addToQuiver(quiver: ItemStack, stack: ItemStack): Int {
        // TODO: add the arrow on a leash to the arrows tag
        if (!stack.isEmpty && (stack.isIn(ItemTags.ARROWS) || stack.item is LeashedArrow)) {
            val compound = quiver.orCreateNbt
            if (!compound.contains(ITEMS_KEY)) {
                compound.put(ITEMS_KEY, NbtList())
            }

            val i = this.getQuiverOccupancy(quiver)
            val j = 64 / stack.maxCount
            val k = stack.count.coerceAtMost((64 - i) / j)
            if (k == 0) {
                return 0
            } else {
                val nbtList = compound.getList("Items", NbtElement.COMPOUND_TYPE.toInt())
                val optional = canMergeStack(stack, nbtList)
                if (optional.isPresent) {
                    val nbtCompound2 = optional.get()
                    val itemStack = ItemStack.fromNbt(nbtCompound2)
                    itemStack.increment(k)
                    itemStack.writeNbt(nbtCompound2)
                    nbtList.remove(nbtCompound2)
                    nbtList.add(0, nbtCompound2)
                } else {
                    val itemStack2 = stack.withCount(k)
                    val nbtCompound3 = NbtCompound()
                    itemStack2.writeNbt(nbtCompound3)
                    nbtList.add(0, nbtCompound3)
                }
            }

            return k
        }

        return 0
    }

    override fun getTooltipData(stack: ItemStack): Optional<TooltipData>? {
        val defaultedList = DefaultedList.of<ItemStack>()
        getBundledStacks(stack).forEach { e: ItemStack ->
            defaultedList.add(
                e
            )
        }
        return Optional.of(
            BundleTooltipData(
                defaultedList,
                getQuiverOccupancy(stack)
            )
        )
    }

    fun getQuiverOccupancy(stack: ItemStack) =
        getBundledStacks(stack).mapToInt { itemStack: ItemStack ->
            (64 / stack.maxCount) * itemStack.count
        }.sum()

    fun removeFromFirstStack(stack: ItemStack): Optional<ItemStack> {
        val nbtCompound = stack.getOrCreateNbt()
        return if (!nbtCompound.contains("Items")) {
            Optional.empty()
        } else {
            val nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE.toInt())
            if (nbtList.isEmpty()) {
                Optional.empty()
            } else {
                val nbtCompound2 = nbtList.getCompound(0)
                val itemStack = ItemStack.fromNbt(nbtCompound2)

                val arrow = itemStack.split(1)
                if (itemStack.isEmpty) {
                    nbtList.removeAt(0)
                } else {
                    nbtList[0] = itemStack.writeNbt(nbtCompound2)
                }

                if (nbtList.isEmpty()) {
                    stack.removeSubNbt("Items")
                }
                Optional.of(arrow)
            }
        }
    }

    fun currentArrowStack(stack: ItemStack): Optional<ItemStack> {
        val nbtCompound = stack.getOrCreateNbt()
        return if (!nbtCompound.contains("Items")) {
            Optional.empty()
        } else {
            val nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE.toInt())
            if (nbtList.isEmpty()) {
                Optional.empty()
            } else {
                val nbtCompound2 = nbtList.getCompound(0)
                val itemStack = ItemStack.fromNbt(nbtCompound2)
                Optional.of(itemStack)
            }
        }
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        tooltip.add(
            Text.translatable(
                "item.minecraft.bundle.fullness",
                getQuiverOccupancy(stack),
                64
            ).formatted(Formatting.GRAY)
        )
    }
}
