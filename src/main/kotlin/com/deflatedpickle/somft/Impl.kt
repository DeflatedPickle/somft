/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER", "SpellCheckingInspection")

package com.deflatedpickle.somft

import com.deflatedpickle.somft.api.Anchor
import com.deflatedpickle.somft.api.HotbarLayout
import com.deflatedpickle.somft.api.IconSet
import com.deflatedpickle.somft.api.IconType.EMPTY
import com.deflatedpickle.somft.api.IconType.FULL
import com.deflatedpickle.somft.api.IconType.HALF
import com.deflatedpickle.somft.api.Milkable
import com.deflatedpickle.somft.client.tooltip.CentredTooltipPositioner
import com.deflatedpickle.somft.screen.ArmorSlot
import com.deflatedpickle.somft.screen.OffHandSlot
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CarvedPumpkinBlock
import net.minecraft.block.FireBlock
import net.minecraft.block.RedstoneOreBlock
import net.minecraft.block.cauldron.LeveledCauldronBlock
import net.minecraft.block.dispenser.DispenserBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.enchantment.DamageEnchantment
import net.minecraft.enchantment.DepthStriderEnchantment
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.enchantment.FrostWalkerEnchantment
import net.minecraft.enchantment.InfinityEnchantment
import net.minecraft.enchantment.LuckEnchantment
import net.minecraft.enchantment.MultishotEnchantment
import net.minecraft.enchantment.PiercingEnchantment
import net.minecraft.enchantment.ProtectionEnchantment
import net.minecraft.enchantment.RiptideEnchantment
import net.minecraft.enchantment.SilkTouchEnchantment
import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.decoration.DisplayEntity
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.entity.decoration.painting.PaintingEntity
import net.minecraft.entity.effect.StatusEffectUtil
import net.minecraft.entity.mob.BlazeEntity
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.mob.GhastEntity
import net.minecraft.entity.mob.GuardianEntity
import net.minecraft.entity.mob.HoglinEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.PhantomEntity
import net.minecraft.entity.mob.PiglinEntity
import net.minecraft.entity.mob.PillagerEntity
import net.minecraft.entity.mob.SkeletonEntity
import net.minecraft.entity.mob.SpellcastingIllagerEntity
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.entity.mob.VexEntity
import net.minecraft.entity.mob.ZoglinEntity
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.entity.mob.ZombieVillagerEntity
import net.minecraft.entity.mob.warden.WardenEntity
import net.minecraft.entity.passive.AbstractDonkeyEntity
import net.minecraft.entity.passive.AllayEntity
import net.minecraft.entity.passive.AxolotlEntity
import net.minecraft.entity.passive.BatEntity
import net.minecraft.entity.passive.BeeEntity
import net.minecraft.entity.passive.CamelEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.CowEntity
import net.minecraft.entity.passive.DolphinEntity
import net.minecraft.entity.passive.FishEntity
import net.minecraft.entity.passive.FoxEntity
import net.minecraft.entity.passive.FrogEntity
import net.minecraft.entity.passive.GlowSquidEntity
import net.minecraft.entity.passive.GoatEntity
import net.minecraft.entity.passive.HorseBaseEntity
import net.minecraft.entity.passive.HorseEntity
import net.minecraft.entity.passive.LlamaEntity
import net.minecraft.entity.passive.MerchantEntity
import net.minecraft.entity.passive.MooshroomEntity
import net.minecraft.entity.passive.OcelotEntity
import net.minecraft.entity.passive.PandaEntity
import net.minecraft.entity.passive.ParrotEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.passive.PigEntity
import net.minecraft.entity.passive.PolarBearEntity
import net.minecraft.entity.passive.PufferfishEntity
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.passive.SnifferEntity
import net.minecraft.entity.passive.StriderEntity
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.entity.passive.TropicalFishEntity
import net.minecraft.entity.passive.TurtleEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.projectile.AbstractFireballEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.raid.RaiderEntity
import net.minecraft.entity.vehicle.AbstractMinecartEntity
import net.minecraft.entity.vehicle.BoatEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.BlockItem
import net.minecraft.item.BucketItem
import net.minecraft.item.DyeItem
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.FireChargeItem
import net.minecraft.item.FlintAndSteelItem
import net.minecraft.item.HoeItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.Items
import net.minecraft.item.PotionItem
import net.minecraft.item.PowderSnowBucketItem
import net.minecraft.item.ShearsItem
import net.minecraft.item.TippedArrowItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.potion.PotionUtil
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Axis
import net.minecraft.util.math.BlockPointer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import org.joml.Vector2f
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.cos
import kotlin.math.sin

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
                world.gameRules.getBoolean(Somft.DO_MOB_FIRE_GRIEF)
            ) {
                val increase = if (world.getBiome(entity.blockPos).isIn(BiomeTags.INCREASED_FIRE_BURNOUT)) -50 else 0
                (Blocks.FIRE as FireBlock).trySpreadingFire(world, entity.blockPos.down(), 300 + increase, entity.random, 0)
            }
        }
    }

    fun drawExtraTitleComponents(
        screen: TitleScreen,
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        textRenderer: TextRenderer,
        width: Int,
        height: Int,
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
        val armour = instance.armorItems.map { it.item }.filterIsInstance<ArmorItem>().filter { it.material != ArmorMaterials.CHAIN }

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
        } else if (block == Blocks.PUMPKIN) {
            if (item is ShearsItem) {
                if (!world.isClient) {
                    val facing = blockState.get(DispenserBlock.FACING).opposite

                    world.playSound(null, blockPos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    world.setBlockState(
                        blockPos,
                        Blocks.CARVED_PUMPKIN.defaultState.with(
                            CarvedPumpkinBlock.FACING,
                            facing
                        ),
                        Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD
                    )
                    val itemEntity = ItemEntity(
                        world,
                        blockPos.x.toDouble() + 0.5 + facing.offsetX.toDouble() * 0.65,
                        blockPos.y.toDouble() + 0.1,
                        blockPos.z.toDouble() + 0.5 + facing.offsetZ.toDouble() * 0.65,
                        ItemStack(Items.PUMPKIN_SEEDS, 4)
                    )
                    itemEntity.setVelocity(
                        0.05 * facing.offsetX.toDouble() + world.random.nextDouble() * 0.02,
                        0.05,
                        0.05 * facing.offsetZ.toDouble() + world.random.nextDouble() * 0.02
                    )
                    world.spawnEntity(itemEntity)
                    itemStack.damage(1, world.random, null)
                    world.emitGameEvent(null, GameEvent.SHEAR, blockPos)
                }

                cir.returnValue = itemStack
            }
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

        dispenserCow(world, blockPos, blockState, block, itemStack, item, cir)
        dispenserSheep(world, blockPos, blockState, block, itemStack, item, cir)
    }

    private fun dispenserCow(
        world: World,
        blockPos: BlockPos,
        blockState: BlockState,
        block: Block,
        itemStack: ItemStack,
        item: Item,
        cir: CallbackInfoReturnable<ItemStack>
    ) {
        for (
            cow in world
                .getEntitiesByClass(
                    CowEntity::class.java, Box(blockPos)
                ) { entity: CowEntity -> entity.isAlive && !entity.isBaby }
        ) {
            if (item == Items.BUCKET) {
                if ((cow as Milkable).`somft$getMilkTicks`() <= 0) {
                    (cow as Milkable).`somft$setMilkTicks`(24000 / 5)
                    cir.returnValue = Items.MILK_BUCKET.defaultStack
                } else {
                    world.playSound(null, blockPos, SoundEvents.ENTITY_COW_MILK, SoundCategory.PLAYERS, 1.0f, 1.0f)
                    spawnPlayerReactionParticles(cow)
                    cir.returnValue = itemStack
                }
            }
        }
    }

    private fun dispenserSheep(
        world: World,
        blockPos: BlockPos,
        blockState: BlockState,
        block: Block,
        itemStack: ItemStack,
        item: Item,
        cir: CallbackInfoReturnable<ItemStack>
    ) {
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

    fun dispenserShears(
        pointer: BlockPointer,
        world: ServerWorld
    ): Boolean {
        val dispenserPos = pointer.pos
        val dispenserState = world.getBlockState(dispenserPos)
        val blockPos = pointer.pos.offset(pointer.blockState.get(DispenserBlock.FACING))
        val state = world.getBlockState(blockPos)

        if (state.block == Blocks.PUMPKIN) {
            val facing = dispenserState.get(DispenserBlock.FACING).opposite

            world.playSound(null, blockPos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0f, 1.0f)
            world.setBlockState(
                blockPos,
                Blocks.CARVED_PUMPKIN.defaultState.with(CarvedPumpkinBlock.FACING, facing),
                Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD
            )
            val itemEntity = ItemEntity(
                world,
                blockPos.x.toDouble() + 0.5 + facing.offsetX.toDouble() * 0.65,
                blockPos.y.toDouble() + 0.1,
                blockPos.z.toDouble() + 0.5 + facing.offsetZ.toDouble() * 0.65,
                ItemStack(Items.PUMPKIN_SEEDS, 4)
            )
            itemEntity.setVelocity(
                0.05 * facing.offsetX.toDouble() + world.random.nextDouble() * 0.02,
                0.05,
                0.05 * facing.offsetZ.toDouble() + world.random.nextDouble() * 0.02
            )
            world.spawnEntity(itemEntity)
            world.emitGameEvent(null, GameEvent.SHEAR, blockPos)

            return true
        }
        return false
    }

    fun translateAndHideDebugText(instance: MutableList<String>, e: String): Boolean {
        return if (MinecraftClient.getInstance().player?.isCreativeLevelTwoOp == true) {
            instance.add(e)
        } else false
    }

    // TODO: make these translatable? ;-;
    fun getTrackedDataName(trackedData: TrackedData<*>) = when (trackedData) {
        Entity.FLAGS -> "Flags"
        Entity.AIR -> "Air"
        Entity.CUSTOM_NAME -> "Custom Name"
        Entity.NAME_VISIBLE -> "Name Visible"
        Entity.SILENT -> "Silent"
        Entity.NO_GRAVITY -> "No Gravity"
        Entity.POSE -> "Pose"
        Entity.FROZEN_TICKS -> "Frozen Ticks"
        LivingEntity.LIVING_FLAGS -> "Living Flags"
        LivingEntity.HEALTH -> "Health"
        LivingEntity.POTION_SWIRLS_COLOR -> "Potion Swirls Color"
        LivingEntity.POTION_SWIRLS_AMBIENT -> "Potion Swirls Ambient"
        LivingEntity.STUCK_ARROW_COUNT -> "Stuck Arrow Count"
        LivingEntity.STINGER_COUNT -> "Stinger Count"
        LivingEntity.SLEEPING_POSITION -> "Sleeping Position"
        MobEntity.MOB_FLAGS -> "Mob Flags"
        PassiveEntity.CHILD -> "Child"
        AbstractDonkeyEntity.CHEST -> "Chest"
        AxolotlEntity.VARIANT -> "Variant"
        AxolotlEntity.PLAYING_DEAD -> "Playing Dead"
        AxolotlEntity.FROM_BUCKET -> "From Bucket"
        BeeEntity.BEE_FLAGS -> "Bee Flags"
        BeeEntity.ANGER -> "Anger"
        CamelEntity.DASHING -> "Dashing"
        CamelEntity.LAST_ANIMATION_TICK -> "Last Animation Tick"
        CatEntity.VARIANT -> "Variant"
        CatEntity.IN_SLEEPING_POSE -> "In Sleeping Pose"
        CatEntity.HEAD_DOWN -> "Head Down"
        CatEntity.COLLAR_COLOR -> "Collar Color"
        FoxEntity.TYPE -> "Type"
        FoxEntity.FOX_FLAGS -> "Fox Flags"
        FrogEntity.TYPE -> "Type"
        FrogEntity.TARGET_ENTITY_ID -> "Target Entity ID"
        GoatEntity.SCREAMING -> "Screaming"
        GoatEntity.HAS_LEFT_HORN -> "Has Left Horn"
        GoatEntity.HAS_RIGHT_HORN -> "Has Right Horn"
        HoglinEntity.BABY -> "Baby"
        HorseBaseEntity.HORSE_FLAGS -> "Horse Flags"
        HorseEntity.VARIANT -> "Variant"
        LlamaEntity.STRENGTH -> "Strength"
        LlamaEntity.CARPET_COLOR -> "Carpet Color"
        LlamaEntity.VARIANT -> "Variant"
        MooshroomEntity.TYPE -> "Type"
        OcelotEntity.TRUSTING -> "Trusting"
        PandaEntity.ASK_FOR_BAMBOO_TICKS -> "Ask For Bamboo Ticks"
        PandaEntity.SNEEZE_PROGRESS -> "Sneeze Progress"
        PandaEntity.EATING_TICKS -> "Eating Ticks"
        PandaEntity.MAIN_GENE -> "Main Gene"
        PandaEntity.HIDDEN_GENE -> "Hidden Gene"
        PandaEntity.PANDA_FLAGS -> "Panda Flags"
        ParrotEntity.VARIANT -> "Variant"
        PigEntity.SADDLED -> "Saddled"
        PigEntity.BOOST_TIME -> "Boost Time"
        PolarBearEntity.WARNING -> "Warning"
        RabbitEntity.RABBIT_TYPE -> "Rabbit Type"
        SheepEntity.COLOR -> "Color"
        SnifferEntity.STATE -> "State"
        SnifferEntity.DROP_SEED_AT_TICK -> "Drop Seed At Tick"
        StriderEntity.BOOST_TIME -> "Boost Time"
        StriderEntity.COLD -> "Cold"
        StriderEntity.SADDLED -> "Saddled"
        TameableEntity.TAMEABLE_FLAGS -> "Tameable Flags"
        TameableEntity.OWNER_UUID -> "Owner UUID"
        TurtleEntity.HOME_POS -> "Home Pos"
        TurtleEntity.HAS_EGG -> "Has Egg"
        TurtleEntity.DIGGING_SAND -> "Digging Sand"
        TurtleEntity.TRAVEL_POS -> "Travel Pos"
        TurtleEntity.LAND_BOUND -> "Land Bound"
        TurtleEntity.ACTIVELY_TRAVELING -> "Actively Traveling"
        WolfEntity.BEGGING -> "Begging"
        WolfEntity.COLLAR_COLOR -> "Collar Color"
        WolfEntity.ANGER_TIME -> "Anger Time"
        ItemFrameEntity.ITEM_STACK -> "Item Stack"
        ItemFrameEntity.ROTATION -> "Rotation"
        PaintingEntity.VARIANT -> "Variant"
        AbstractFireballEntity.ITEM -> "Item"
        AbstractMinecartEntity.DAMAGE_WOBBLE_TICKS -> "Damage Wobble Ticks"
        AbstractMinecartEntity.DAMAGE_WOBBLE_SIDE -> "Damage Wobble Side"
        AbstractMinecartEntity.DAMAGE_WOBBLE_STRENGTH -> "Damage Wobble Strength"
        AbstractMinecartEntity.CUSTOM_BLOCK_ID -> "Custom Block ID"
        AbstractMinecartEntity.CUSTOM_BLOCK_OFFSET -> "Custom Block Offset"
        AbstractMinecartEntity.CUSTOM_BLOCK_PRESENT -> "Custom Block Present"
        PiglinEntity.IMMUNE_TO_ZOMBIFICATION -> "Immune To Zombification"
        AllayEntity.DANCING -> "Dancing"
        AllayEntity.CAN_DUPLICATE -> "Can Duplicate"
        AreaEffectCloudEntity.RADIUS -> "Radius"
        AreaEffectCloudEntity.COLOR -> "Color"
        AreaEffectCloudEntity.WAITING -> "Waiting"
        AreaEffectCloudEntity.PARTICLE_ID -> "Particle ID"
        ArmorStandEntity.ARMOR_STAND_FLAGS -> "Armor Stand Flags"
        ArmorStandEntity.TRACKER_HEAD_ROTATION -> "Head Rotation"
        ArmorStandEntity.TRACKER_BODY_ROTATION -> "Body Rotation"
        ArmorStandEntity.TRACKER_LEFT_ARM_ROTATION -> "Left Arm Rotation"
        ArmorStandEntity.TRACKER_RIGHT_ARM_ROTATION -> "Right Arm Rotation"
        ArmorStandEntity.TRACKER_LEFT_LEG_ROTATION -> "Left Leg Rotation"
        ArmorStandEntity.TRACKER_RIGHT_LEG_ROTATION -> "Right Leg Rotation"
        ArrowEntity.COLOR -> "Color"
        BatEntity.BAT_FLAGS -> "Bat Flags"
        BlazeEntity.BLAZE_FLAGS -> "Blaze Flags"
        CreeperEntity.FUSE_SPEED -> "Fuse Speed"
        CreeperEntity.CHARGED -> "Charged"
        CreeperEntity.IGNITED -> "Ignited"
        EndermanEntity.CARRIED_BLOCK -> "Carried Block"
        EndermanEntity.ANGRY -> "Angry"
        EndermanEntity.PROVOKED -> "Provoked"
        GuardianEntity.SPIKES_RETRACTED -> "Spikes Retracted"
        GuardianEntity.BEAM_TARGET_ID -> "Beam Target ID"
        PiglinEntity.BABY -> "Baby"
        PiglinEntity.CHARGING -> "Charging"
        PiglinEntity.DANCING -> "Dancing"
        PillagerEntity.CHARGING -> "Charging"
        RaiderEntity.CELEBRATING -> "Celebrating"
        SkeletonEntity.CONVERTING -> "Converting"
        SpellcastingIllagerEntity.SPELL -> "Spell"
        SpiderEntity.SPIDER_FLAGS -> "Spider Flags"
        VexEntity.VEX_FLAGS -> "Vex Flags"
        WardenEntity.ANGER_LEVEL -> "Anger Level"
        ZoglinEntity.BABY -> "Baby"
        ZombieEntity.BABY -> "Baby"
        ZombieEntity.ZOMBIE_TYPE -> "Zombie Time"
        ZombieEntity.CONVERTING_IN_WATER -> "Converting In Water"
        ZombieVillagerEntity.CONVERTING -> "Converting"
        ZombieVillagerEntity.VILLAGER_DATA -> "Villager Data"
        BoatEntity.DAMAGE_WOBBLE_TICKS -> "Damage Wobble Ticks"
        BoatEntity.DAMAGE_WOBBLE_SIDE -> "Damage Wobble Side"
        BoatEntity.DAMAGE_WOBBLE_STRENGTH -> "Damage Wobble Strength"
        BoatEntity.BOAT_VARIANT -> "Boat Variant"
        BoatEntity.LEFT_PADDLE_MOVING -> "Left Paddle Moving"
        BoatEntity.RIGHT_PADDLE_MOVING -> "Right Paddle Moving"
        BoatEntity.BUBBLE_WOBBLE_TICKS -> "Bubble Wobble Ticks"
        DisplayEntity.TextDisplayEntity.TEXT -> "Text"
        DisplayEntity.TextDisplayEntity.LINE_WIDTH -> "Line Width"
        DisplayEntity.TextDisplayEntity.BACKGROUND_COLOR -> "Background Color"
        DisplayEntity.BlockDisplayEntity.STATE -> "State"
        DisplayEntity.START_INTERPOLATION -> "Start Interpolation"
        DisplayEntity.INTERPOLATION_DURATION -> "Interpolation Duration"
        DisplayEntity.TRANSLATION -> "Translation"
        DisplayEntity.SCALE -> "Scale"
        DisplayEntity.LEFT_ROTATION -> "Left Rotation"
        DisplayEntity.RIGHT_ROTATION -> "Right Rotation"
        DisplayEntity.BILLBOARD_RENDER_CONSTRAINTS -> "Billboard Render Constraints"
        DisplayEntity.BRIGHTNESS_OVERRIDE -> "Brightness Override"
        DisplayEntity.VIEW_RANGE -> "View Range"
        DisplayEntity.SHADOW_RADIUS -> "Shadow Radius"
        DisplayEntity.SHADOW_STRENGTH -> "Shadow Strength"
        DisplayEntity.WIDTH -> "Width"
        DisplayEntity.HEIGHT -> "Height"
        DisplayEntity.GLOW_COLOR_OVERRIDE -> "Glow Color Override"
        DisplayEntity.ItemDisplayEntity.STACK -> "Stack"
        DisplayEntity.ItemDisplayEntity.TRANSFORMATION_MODE -> "Transformation Mode"
        FishEntity.FROM_BUCKET -> "From Bucket"
        GhastEntity.SHOOTING -> "Shooting"
        PhantomEntity.SIZE -> "Size"
        DolphinEntity.TREASURE_POS -> "Treasure Pos"
        DolphinEntity.HAS_FISH -> "Has Fish"
        DolphinEntity.MOISTNESS -> "Moistness"
        GlowSquidEntity.DARK_TICKS_REMAINING -> "Dark Ticks Remaining"
        PufferfishEntity.PUFF_STATE -> "Puff State"
        TropicalFishEntity.VARIANT -> "Variant"
        VillagerEntity.VILLAGER_DATA -> "Villager Data"
        MerchantEntity.HEAD_ROLLING_TIME_LEFT -> "Head Rolling Time Left"
        else -> ""
    }

    fun entryToString(entry: DataTracker.Entry<*>) = "${getTrackedDataName(entry.data)} (${entry.data.id}): ${entry.get()}"

    fun drawTip(
        graphics: GuiGraphics,
        textRenderer: TextRenderer,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
        ci: CallbackInfo,
        ms: Long,
        midX: Int,
        midY: Int,
        tip: String,
    ) {
        val text = Text.literal(I18n.translate(tip)).asOrderedText()
        val tooltip = TooltipComponent.of(text)

        graphics.drawTooltip(
            textRenderer,
            listOf(tooltip),
            midX, midY + 60,
            CentredTooltipPositioner
        )
        graphics.matrices.translate(0f, 0f, 401f)
        graphics.drawCenteredShadowedText(
            textRenderer,
            "#${SomftClient.tooltips.indexOf(tip)}",
            midX + tooltip.getWidth(textRenderer) / 2,
            midY - tooltip.height + textRenderer.fontHeight / 2 + 60,
            DyeColor.YELLOW.signColor
        )
    }

    fun drawBackground(
        screen: Screen,
        graphics: GuiGraphics,
        backgroundTexture: Identifier,
        x: Int,
        y: Int
    ) {
        // armor slots
        graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x - 20, y, 0, 0, 25, 79)
        // off-hand background
        graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x - 13, y + 7 + 18 * 4, 7, 7, 18, 18)
        // off-hand edge
        graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x - 20, y + 7 + 18 * 4, 0, 4, 7, 18)
        // removes a bit of white
        graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x + 1, y + 79 + 18, 3, 159, 2, 4)
        // bottom edge
        graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x - 20, y + 79 + 18, 0, 159, 21, 6)
        // a lil gray pixel to make it look like a corner
        graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x + 1, y + 83 + 18, 3, 164, 1, 1)

        if (screen is CreativeInventoryScreen && CreativeInventoryScreen.selectedTab == ItemGroups.getDefaultTab()) {
            // white part
            graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x + 1, y, 1, 159, 2, 3)
            // gray part
            graphics.drawTexture(HandledScreen.BACKGROUND_TEXTURE, x + 3, y, 3, 159, 2, 3)
        }
    }

    fun outsideExtraSlotBounds(
        mouseX: Double,
        mouseY: Double,
        left: Int,
        top: Int,
        button: Int
    ) = mouseX < left - 20 ||
        mouseY < top ||
        mouseX >= left ||
        mouseY >= top + 79 + 18 + 7

    fun addExtraSlots(screen: ScreenHandler, playerInventory: PlayerInventory) {
        for (i in 0..3) {
            screen.addSlot(
                ArmorSlot(
                    playerInventory, playerInventory.player,
                    PlayerScreenHandler.EQUIPMENT_SLOT_ORDER[i]
                )
            )
        }

        screen.addSlot(OffHandSlot(playerInventory, playerInventory.player))
    }

    val ICONS = Identifier("textures/gui/icons.png")

    fun drawIconRow(
        graphics: GuiGraphics,
        textRenderer: TextRenderer,
        limit: Int,
        x: Int,
        y: Int,
        set: IconSet,
    ) {
        for (s in 0..9) {
            val x = x + if (set.backwards) 10 * 8 - s * 8 - 9 else s * 8
            val (u, v) = set.getUV(EMPTY)
            graphics.drawTexture(
                ICONS,
                x, y,
                u, v,
                9, 9
            )

            if (s * 2 + 1 < limit) {
                val (u, v) = set.getUV(FULL)
                graphics.drawTexture(
                    ICONS,
                    x, y,
                    u, v,
                    9, 9
                )
            }

            if (s * 2 + 1 == limit) {
                val (u, v) = set.getUV(HALF)
                graphics.drawTexture(
                    ICONS,
                    x, y,
                    u, v,
                    9, 9
                )
            }
        }
    }

    val HOTBAR_TEXTURE = Identifier("somft", "textures/gui/hotbar.png")

    fun renderHotbar(
        instance: InGameHud,
        tickDelta: Float,
        graphics: GuiGraphics,
        renderHotbarItem: (
            graphics: GuiGraphics,
            x: Int,
            y: Int,
            tickDelta: Float,
            player: PlayerEntity,
            stack: ItemStack,
            seed: Int
        ) -> Unit
    ) {
        // TODO: animate (fade, expand, move) in/out
        // TODO: more shapes

        var seed = 1

        graphics.matrices.apply {
            push()

            val pos = Vector2f(0f, 0f)
            val hPad = SomftClient.CONFIG.hudConfig.hotbarConfig.horizontalPadding
            val vPad = SomftClient.CONFIG.hudConfig.hotbarConfig.verticalPadding
            when (SomftClient.CONFIG.hudConfig.hotbarConfig.anchor) {
                Anchor.ROSE -> pos.set(graphics.scaledWindowWidth / 2f, graphics.scaledWindowHeight / 2f)
                Anchor.N -> pos.set(graphics.scaledWindowWidth / 2f, vPad)
                Anchor.NE -> pos.set(graphics.scaledWindowWidth.toFloat() - hPad, vPad)
                Anchor.E -> pos.set(graphics.scaledWindowWidth.toFloat() - hPad, graphics.scaledWindowHeight / 2f)
                Anchor.SE -> pos.set(graphics.scaledWindowWidth.toFloat() - hPad, graphics.scaledWindowHeight.toFloat() - vPad)
                Anchor.S -> pos.set(graphics.scaledWindowWidth / 2f, graphics.scaledWindowHeight.toFloat() - vPad)
                Anchor.SW -> pos.set(hPad, graphics.scaledWindowHeight.toFloat() - vPad)
                Anchor.W -> pos.set(hPad, graphics.scaledWindowHeight / 2f)
                Anchor.NW -> pos.set(hPad, vPad)
            }
            translate(pos.x, pos.y, 0f)

            for (i in 0 until 9) {
                var x = 0
                var y = 0

                when (SomftClient.CONFIG.hudConfig.hotbarConfig.layout) {
                    HotbarLayout.ROW -> {
                        x = -91 + i * 20
                        y = -12
                    }
                    HotbarLayout.COLUMN -> {
                        x = -11
                        y = -92 + i * 20
                    }
                    HotbarLayout.CIRCLE -> {
                        val radius = 2 * 22
                        val theta = (Math.PI * 2) * i / 9
                        x = (-11f + radius * cos(theta)).toInt()
                        y = (-11f + radius * sin(theta)).toInt()
                    }
                }

                drawSlot(tickDelta, graphics, seed++, x, y, i, renderHotbarItem)
            }

            pop()
        }
    }

    fun drawSlot(
        tickDelta: Float,
        graphics: GuiGraphics,
        seed: Int,
        x: Int,
        y: Int,
        i: Int,
        renderHotbarItem: (
            graphics: GuiGraphics,
            x: Int,
            y: Int,
            tickDelta: Float,
            player: PlayerEntity,
            stack: ItemStack,
            seed: Int
        ) -> Unit
    ) {
        when (SomftClient.CONFIG.hudConfig.hotbarConfig.layout) {
            HotbarLayout.ROW -> graphics.drawTexture(
                HOTBAR_TEXTURE,
                x, y, 0,
                when (i) {
                    0 -> 0f
                    8 -> 41f
                    else -> 21f
                },
                0f,
                21, 22,
                88, 64
            )
            HotbarLayout.COLUMN -> graphics.drawTexture(
                HOTBAR_TEXTURE,
                x, y, 0,
                66f,
                when (i) {
                    0 -> 0f
                    8 -> 41f
                    else -> 21f
                },
                21, 22,
                88, 64
            )
            HotbarLayout.CIRCLE -> graphics.drawTexture(
                HOTBAR_TEXTURE,
                x, y, 0,
                24f, 22f,
                22, 22,
                88, 64
            )
        }

        MinecraftClient.getInstance().let { client ->
            client.cameraEntity?.let { entity ->
                if (entity is PlayerEntity) {
                    if (i == entity.inventory.selectedSlot) {
                        graphics.drawTexture(
                            HOTBAR_TEXTURE,
                            x - 1, y - 1, 1,
                            0f, 22f,
                            24, 24,
                            88, 64
                        )
                    }

                    val plus = if (SomftClient.CONFIG.hudConfig.hotbarConfig.layout == HotbarLayout.CIRCLE) 3 else 1
                    renderHotbarItem(
                        graphics,
                        x + plus,
                        y + plus + 1,
                        tickDelta,
                        entity,
                        entity.inventory.main[i],
                        seed
                    )
                }
            }
        }
    }

    fun getIncompatible(enchantment: Enchantment): List<Enchantment> =
        when (enchantment) {
            is DamageEnchantment -> mutableListOf(
                Enchantments.SHARPNESS,
                Enchantments.SMITE,
                Enchantments.BANE_OF_ARTHROPODS,
            ).apply {
                remove(enchantment)
            }
            is DepthStriderEnchantment -> listOf(Enchantments.FROST_WALKER)
            is FrostWalkerEnchantment -> listOf(Enchantments.DEPTH_STRIDER)
            is InfinityEnchantment -> listOf(Enchantments.MENDING)
            is LuckEnchantment -> listOf(Enchantments.SILK_TOUCH)
            is MultishotEnchantment -> listOf(Enchantments.PIERCING)
            is PiercingEnchantment -> listOf(Enchantments.MULTISHOT)
            is ProtectionEnchantment -> mutableListOf(
                Enchantments.PROTECTION,
                Enchantments.FIRE_PROTECTION,
                Enchantments.FEATHER_FALLING,
                Enchantments.BLAST_PROTECTION,
                Enchantments.PROJECTILE_PROTECTION,
            ).apply {
                remove(enchantment)
            }
            is RiptideEnchantment -> listOf(
                Enchantments.LOYALTY,
                Enchantments.CHANNELING,
            )
            is SilkTouchEnchantment -> listOf(Enchantments.FORTUNE)
            else -> listOf()
        }
}
