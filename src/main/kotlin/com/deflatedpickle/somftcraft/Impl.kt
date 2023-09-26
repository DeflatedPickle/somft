/* Copyright (c) 2023 DeflatedPickle under the MIT license */

package com.deflatedpickle.somftcraft

import com.mojang.datafixers.util.Either
import net.minecraft.block.Blocks
import net.minecraft.block.FireBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectUtil
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.vehicle.AbstractMinecartEntity
import net.minecraft.entity.vehicle.BoatEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ArmorItem
import net.minecraft.item.BlockItem
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.FireChargeItem
import net.minecraft.item.FlintAndSteelItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.PotionItem
import net.minecraft.item.TippedArrowItem
import net.minecraft.potion.PotionUtil
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.Axis
import net.minecraft.world.GameRules
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

object Impl {
    fun igniteEntity(
        stack: ItemStack,
        player: PlayerEntity,
        entity: LivingEntity,
        hand: Hand
    ): ActionResult {
        val sound = when (stack.item) {
            is FlintAndSteelItem -> SoundEvents.ITEM_FLINTANDSTEEL_USE
            is FireChargeItem -> SoundEvents.ITEM_FIRECHARGE_USE
            else -> SoundEvents.ENTITY_GENERIC_BURN
        }

        with(player.world) {
            playSound(
                player,
                entity.blockPos,
                sound,
                SoundCategory.BLOCKS,
                1.0f,
                getRandom().nextFloat() * 0.4f + 0.8f
            )

            if (entity.isFireImmune) return ActionResult.FAIL

            entity.fireTicks += 1

            if (entity.fireTicks <= 0) {
                entity.setOnFireFor(8)
            }

            entity.damage(damageSources.onFire(), 1f)
            stack.damage(1, player) { p: PlayerEntity -> p.sendToolBreakStatus(hand) }

            return ActionResult.success(isClient)
        }
    }

    fun entityFireSpread(
        entity: Entity
    ) {
        entity.world.let { world ->
            if (!world.isClient &&
                entity.isOnFire &&
                world.gameRules.getBoolean(GameRules.DO_FIRE_TICK)
            ) {
                val increase = if (world.getBiome(entity.blockPos).isIn(BiomeTags.INCREASED_FIRE_BURNOUT)) -50 else 0
                (Blocks.FIRE as FireBlock).trySpreadingFire(world, entity.blockPos.down(), 300 + increase, entity.random, 0)
            }
        }
    }

    fun drawExtraTitleComponents(
        graphics: GuiGraphics,
        textRenderer: TextRenderer,
        width: Int,
        alpha: Int
    ) {
        graphics.matrices.push()
        graphics.matrices.translate(width / 2.0 - 12, 63.0, 0.0)
        graphics.matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(15f))
        graphics.drawCenteredShadowedText(
            textRenderer,
            Text.literal(":3"),
            0, 0,
            DyeColor.PINK.signColor or alpha
        )
        graphics.matrices.pop()
    }

    fun moveItemText(
        matrices: MatrixStack,
        info: CallbackInfo
    ) {
        val mc = MinecraftClient.getInstance()
        val stack = mc.player!!.mainHandStack

        if (!stack.hasNbt()) return
        if (MinecraftClient.getInstance().interactionManager?.hasStatusBars() == false) return

        if (info.id == "move:head") {
            matrices.push()
            matrices.translate(0.0, -10.0, 0.0)
        } else matrices.pop()
    }

    fun drawItemEnchantments(
        graphics: GuiGraphics,
        width: Int,
        x: Int,
        y: Int,
        opacity: Int
    ) {
        val mc = MinecraftClient.getInstance()
        val textRenderer = mc.textRenderer
        val currentStack = mc.player!!.mainHandStack

        val textList = mutableListOf<Text>()

        if (currentStack.item == Items.SHULKER_BOX) {
            BlockItem.getBlockEntityNbtFromStack(currentStack)?.let { nbtCompound ->
                println(nbtCompound)
                if (nbtCompound.contains("Items")) {
                    val defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY)
                    Inventories.readNbt(nbtCompound, defaultedList)

                    for (i in defaultedList) {
                        if (i.isEmpty) continue
                        val text = i.name.copyContentOnly()
                        text.append(" x").append(i.count.toString())
                        textList.add(text)
                    }
                }
            }
        } else if (currentStack.item is PotionItem || currentStack.item is TippedArrowItem) {
            val effects = PotionUtil.getPotionEffects(currentStack)

            if (effects.isEmpty()) {
                textList.add(Text.translatable("effect.none"))
            } else {
                for (i in effects) {
                    var text = Text.translatable(i.translationKey)
                    if (i.duration > 20) {
                        text = Text.translatable(
                            "potion.withDuration",
                            text,
                            StatusEffectUtil.durationToString(
                                i,
                                when (currentStack.item) {
                                    is PotionItem -> 1.0f
                                    is TippedArrowItem -> 0.125f
                                    else -> 0.0f
                                }
                            )
                        )
                    }
                    textList.add(text)
                }
            }
        } else if (currentStack.item == Items.ENCHANTED_BOOK || currentStack.hasEnchantments()) {
            ItemStack.appendEnchantments(
                textList,
                when (currentStack.item) {
                    is EnchantedBookItem -> EnchantedBookItem.getEnchantmentNbt(currentStack)
                    else -> currentStack.enchantments
                }
            )
        }

        val text = Text.literal("").apply {
            for ((i, s) in textList.take(4).withIndex()) {
                append(s)
                if (i < textList.size - 1) {
                    append(", ")
                }
            }
            if (textList.size > 4) {
                append("...")
            }
        }.formatted(Formatting.ITALIC, Formatting.GRAY)

        val textWidth = textRenderer.getWidth(text)

        graphics.drawShadowedText(
            textRenderer,
            text,
            (x + (width - textWidth) / 2),
            y + 10,
            0xFFFFFF + (opacity shl 24),
        )
    }

    fun redirectSweetBerryBushDamage(
        instance: Entity,
        source: DamageSource,
        amount: Float
    ): Boolean {
        val armour = instance.armorItems.map { it.item }.filterIsInstance<ArmorItem>()

        if (!(
            armour.any { it.armorSlot == ArmorItem.ArmorSlot.BOOTS } &&
                armour.any { it.armorSlot == ArmorItem.ArmorSlot.LEGGINGS }
            )
        ) {
            return instance.damage(source, amount)
        }

        return false
    }

    fun pickupVehicle(
        entity: Either<BoatEntity, AbstractMinecartEntity>,
        player: PlayerEntity,
        cir: CallbackInfoReturnable<ActionResult>?
    ): ActionResult {
        if (player.isSneaking) {
            if (!player.world.isClient) {
                entity
                    .ifLeft {
                        player.giveItemStack(ItemStack(it.asItem()))
                        it.discard()
                    }
                    .ifRight {
                        player.giveItemStack(ItemStack(it.droppedItem))
                        it.discard()
                    }

                cir?.returnValue = ActionResult.CONSUME
                return ActionResult.CONSUME
            }
            cir?.returnValue = ActionResult.SUCCESS
            return ActionResult.SUCCESS
        }

        return ActionResult.PASS
    }
}
