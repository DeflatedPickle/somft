/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft

import com.deflatedpickle.somftcraft.api.Milkable
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FireBlock
import net.minecraft.block.RedstoneOreBlock
import net.minecraft.block.cauldron.LeveledCauldronBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectUtil
import net.minecraft.entity.passive.CowEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.vehicle.AbstractMinecartEntity
import net.minecraft.entity.vehicle.BoatEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ArmorItem
import net.minecraft.item.BlockItem
import net.minecraft.item.BucketItem
import net.minecraft.item.DyeItem
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.FireChargeItem
import net.minecraft.item.FlintAndSteelItem
import net.minecraft.item.HoeItem
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.Items
import net.minecraft.item.PotionItem
import net.minecraft.item.PowderSnowBucketItem
import net.minecraft.item.TippedArrowItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.potion.PotionUtil
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Axis
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Consumer
import java.util.function.Predicate

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

    fun removeArrow(entity: LivingEntity, player: PlayerEntity): Boolean = if (
        player.getStackInHand(player.activeHand).isEmpty &&
        entity.stuckArrowCount - 1 >= 0
    ) {
        if (!entity.world.isClient) {
            player.giveItemStack(ItemStack(Items.ARROW))
            entity.stuckArrowCount -= 1
            entity.damage(player.world.damageSources.playerAttack(player), 1f)
        }
        player.world.playSound(
            entity.x,
            entity.y,
            entity.z,
            SoundEvents.BLOCK_WET_GRASS_STEP,
            SoundCategory.PLAYERS,
            1.0f,
            1.0f,
            false
        )
        RedstoneOreBlock.spawnParticles(player.world, entity.blockPos)

        true
    } else {
        false
    }

    fun dispenseExtra(
        world: World,
        blockPos: BlockPos,
        blockState: BlockState,
        block: Block,
        itemStack: ItemStack,
        item: Item,
        cir: CallbackInfoReturnable<ItemStack>
    ) {
        if (block == Blocks.CAULDRON) {
            if ((item is BucketItem && item != Items.BUCKET) || item is PowderSnowBucketItem) {
                if (!world.isClient) {
                    when (item) {
                        Items.WATER_BUCKET -> {
                            world.setBlockState(
                                blockPos,
                                Blocks.WATER_CAULDRON.defaultState.with(LeveledCauldronBlock.LEVEL, 3)
                            )
                        }
                        Items.LAVA_BUCKET -> {
                            world.setBlockState(blockPos, Blocks.LAVA_CAULDRON.defaultState)
                        }
                        Items.POWDER_SNOW_BUCKET -> {
                            world.setBlockState(
                                blockPos,
                                Blocks.POWDER_SNOW_CAULDRON
                                    .defaultState
                                    .with(LeveledCauldronBlock.LEVEL, 3)
                            )
                        }
                    }
                }

                world.playSound(
                    null, blockPos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f
                )
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos)
                cir.returnValue = ItemStack(Items.BUCKET)
            }
        } else if (block == Blocks.WATER_CAULDRON || block == Blocks.LAVA_CAULDRON || block == Blocks.POWDER_SNOW_CAULDRON) {
            if (item == Items.BUCKET) {
                var bucket: Item? = null
                var sound: SoundEvent? = null

                when (block) {
                    Blocks.WATER_CAULDRON -> {
                        bucket = Items.WATER_BUCKET
                        sound = SoundEvents.ITEM_BUCKET_FILL
                    }

                    Blocks.LAVA_CAULDRON -> {
                        bucket = Items.LAVA_BUCKET
                        sound = SoundEvents.ITEM_BUCKET_FILL_LAVA
                    }

                    Blocks.POWDER_SNOW_CAULDRON -> {
                        bucket = Items.WATER_BUCKET
                        sound = SoundEvents.ITEM_BUCKET_FILL_POWDER_SNOW
                    }
                }

                if (!world.isClient) {
                    world.setBlockState(blockPos, Blocks.CAULDRON.defaultState)
                }

                world.playSound(null, blockPos, sound, SoundCategory.BLOCKS, 1.0f, 1.0f)
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos)
                cir.returnValue = ItemStack(bucket)
            }

            return
        }

        val pos = blockPos.down()
        val state = world.getBlockState(pos)
        val dBlock = state.block
        if (dBlock in HoeItem.TILLING_ACTIONS.keys) {
            if (item is HoeItem) {
                val pair: Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>? =
                    HoeItem.TILLING_ACTIONS[dBlock]
                if (pair != null) {
                    val consumer = pair.second as Consumer<ItemUsageContext>

                    world.playSound(
                        null,
                        blockPos,
                        SoundEvents.ITEM_HOE_TILL,
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f
                    )
                    if (!world.isClient) {
                        consumer.accept(
                            ItemUsageContext(
                                world, null, null,
                                itemStack,
                                BlockHitResult.createMissed(
                                    Vec3d.of(pos), Direction.DOWN, pos
                                )
                            )
                        )
                        itemStack.damage(1, world.random, null)
                    }

                    cir.returnValue = itemStack
                }
            }
        } else if (world.getBlockState(blockPos.down()).block == Blocks.FARMLAND) {
            if (item is HoeItem) {
                cir.returnValue = itemStack
            }

            if (itemStack.isFood) {
                val ctx = ItemPlacementContext(
                    world, null, null,
                    itemStack,
                    BlockHitResult.createMissed(
                        Vec3d.of(blockPos), Direction.DOWN, blockPos
                    )
                )

                if (item is BlockItem) {
                    val placementState = item.getPlacementState(ctx)

                    if (placementState != null) {
                        item.place(ctx)
                        cir.returnValue = itemStack
                    }
                }
            }
        }

        for (
            cow in world
                .getEntitiesByClass(
                    CowEntity::class.java, Box(blockPos)
                ) { entity: CowEntity -> entity.isAlive && !entity.isBaby }
        ) {
            if (item == Items.BUCKET) {
                if ((cow as Milkable).`somftcraft$getMilkTicks`() <= 0) {
                    (cow as Milkable).`somftcraft$setMilkTicks`(24000 / 5)
                    cir.returnValue = Items.MILK_BUCKET.defaultStack
                } else {
                    world.playSound(null, blockPos, SoundEvents.ENTITY_COW_MILK, SoundCategory.PLAYERS, 1.0f, 1.0f)
                    spawnPlayerReactionParticles(cow)
                    cir.returnValue = itemStack
                }
            }
        }

        for (
            sheep in world
                .getEntitiesByClass(
                    SheepEntity::class.java, Box(blockPos)
                ) { entity: SheepEntity -> entity.isAlive && !entity.isSheared }
        ) {
            if (item is DyeItem && sheep.color != item.color) {
                if (!sheep.world.isClient) {
                    sheep.color = item.color
                    itemStack.decrement(1)

                    cir.returnValue = itemStack
                }
            }
        }
    }

    fun spawnPlayerReactionParticles(entity: Entity) {
        for (i in 0..6) {
            val d = entity.random.nextGaussian() * 0.02
            val e = entity.random.nextGaussian() * 0.02
            val f = entity.random.nextGaussian() * 0.02

            entity.world
                .addParticle(
                    ParticleTypes.SMOKE,
                    entity.getParticleX(1.0),
                    entity.randomBodyY + 0.5,
                    entity.getParticleZ(1.0),
                    d,
                    e,
                    f
                )
        }
    }
}
