/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.client.gui.hud

import com.deflatedpickle.somftcraft.SomftCraftClient
import com.deflatedpickle.somftcraft.api.HorizontalTextAnchor
import com.deflatedpickle.somftcraft.api.HorizontalTextAnchor.EAST
import com.deflatedpickle.somftcraft.api.HorizontalTextAnchor.WEST
import com.deflatedpickle.somftcraft.api.LayoutDirection
import com.deflatedpickle.somftcraft.api.TextKind
import com.deflatedpickle.somftcraft.api.VerticalTextAnchor
import com.deflatedpickle.somftcraft.api.VerticalTextAnchor.NORTH
import com.deflatedpickle.somftcraft.api.VerticalTextAnchor.SOUTH
import net.minecraft.block.AbstractButtonBlock
import net.minecraft.block.AbstractFurnaceBlock
import net.minecraft.block.AbstractRailBlock
import net.minecraft.block.AnvilBlock
import net.minecraft.block.BannerBlock
import net.minecraft.block.BarrelBlock
import net.minecraft.block.BeaconBlock
import net.minecraft.block.BedBlock
import net.minecraft.block.BellBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.BrewingStandBlock
import net.minecraft.block.CampfireBlock
import net.minecraft.block.CartographyTableBlock
import net.minecraft.block.ChestBlock
import net.minecraft.block.ComposterBlock
import net.minecraft.block.CraftingTableBlock
import net.minecraft.block.DirtPathBlock
import net.minecraft.block.EnchantingTableBlock
import net.minecraft.block.GrassBlock
import net.minecraft.block.GrindstoneBlock
import net.minecraft.block.HopperBlock
import net.minecraft.block.JukeboxBlock
import net.minecraft.block.LeverBlock
import net.minecraft.block.LoomBlock
import net.minecraft.block.NoteBlock
import net.minecraft.block.RootedDirtBlock
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.block.StonecutterBlock
import net.minecraft.block.cauldron.CauldronBlock
import net.minecraft.block.cauldron.LavaCauldronBlock
import net.minecraft.block.cauldron.LeveledCauldronBlock
import net.minecraft.block.dispenser.DispenserBlock
import net.minecraft.block.entity.BannerBlockEntity
import net.minecraft.block.entity.CampfireBlockEntity
import net.minecraft.block.entity.JukeboxBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.option.KeyBind
import net.minecraft.client.resource.language.I18n
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.entity.passive.CowEntity
import net.minecraft.entity.passive.MerchantEntity
import net.minecraft.entity.passive.PigEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.BlockItem
import net.minecraft.item.BoatItem
import net.minecraft.item.BucketItem
import net.minecraft.item.DyeableItem
import net.minecraft.item.HoeItem
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.Items
import net.minecraft.item.MinecartItem
import net.minecraft.item.MusicDiscItem
import net.minecraft.item.PotionItem
import net.minecraft.item.PowderSnowBucketItem
import net.minecraft.item.RangedWeaponItem
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.recipe.CampfireCookingRecipe
import net.minecraft.text.Text
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import java.util.Optional

class ControlsHud(
    val client: MinecraftClient
) {
    val padding = 5

    fun render(graphics: GuiGraphics) {
        if (this.client.options.debugEnabled || !SomftCraftClient.CONFIG.hudConfig.showControls) return

        val textRenderer = this.client.textRenderer
        val world = this.client.world ?: return
        val player = this.client.player ?: return
        val hand = player.activeHand
        val stack = player.mainHandStack
        val offStack = player.offHandStack
        val item = stack.item
        val itemName = item.name.string

        /*val hotbar = mutableMapOf<String, String>()
        for (i in 0..9) {
            hotbar["key.hotbar.$i"] = "$i"
        }
        drawCategory(graphics, textRenderer, hotbar, CENTRE, SOUTH, LayoutDirection.HORIZONTAL, TextKind.LITERAL)*/

        val menu = mutableMapOf<String, String>()
        // menu[] = "(Esc) Open Menu"
        menu["key.inventory"] = I18n.translate("gui.controls.inventory")
        menu["key.advancements"] = I18n.translate("gui.controls.advancements")
        menu["key.chat"] = I18n.translate("gui.controls.chat")

        if (player.hasPermissionLevel(2)) {
            menu["key.command"] = I18n.translate("gui.controls.command")
        }

        if (!world.isClient) {
            menu["key.playerlist"] = I18n.translate("gui.controls.playerlist")
            menu["key.socialInteractions"] = I18n.translate("gui.controls.socialInteractions")
        }

        // menu["key.screenshot"] = "Take Screenshot"
        // menu["key.togglePerspective"] = "Toggle Perspective"
        // menu["key.smoothCamera"] = "Toggle Smooth Camera"
        // menu["key.fullscreen"] = "Toggle Fullscreen"
        // menu["key.spectatorOutlines"] = "Toggle Spectator Outlines"
        drawCategory(graphics, textRenderer, menu, WEST, NORTH)

        val movements = mutableMapOf<String, String>()
        for (i in listOf("key.jump", "key.sneak", "key.sprint")) {
            movements[i] = Text.translatable(KeyBind.KEY_BINDS[i]?.translationKey).string
        }
        drawCategory(graphics, textRenderer, movements, WEST, SOUTH)

        val interactions = mutableMapOf<String, String>()

        this.client.crosshairTarget?.let { target ->
            when (target.type) {
                HitResult.Type.BLOCK -> {
                    val state = world.getBlockState(BlockPos.fromPosition(target.pos))
                    val block = state.block

                    if (state.isAir) return

                    interactions["key.attack"] = I18n.translate("gui.controls.block.attack")

                    if (player.isCreativeLevelTwoOp) {
                        interactions["key.pickItem"] = I18n.translate("gui.controls.pickItem")
                    }

                    if (item is BoatItem) {
                        interactions["key.use"] = I18n.translate("gui.controls.place")
                    }

                    when (block) {
                        is ChestBlock, is CraftingTableBlock, is AnvilBlock, is AbstractFurnaceBlock, is EnchantingTableBlock,
                        is BrewingStandBlock, is BeaconBlock, is LoomBlock, is BarrelBlock, is CartographyTableBlock,
                        is GrindstoneBlock, is StonecutterBlock, is DispenserBlock, is HopperBlock -> {
                            interactions["key.use"] = I18n.translate("gui.controls.open")
                        }
                        is GrassBlock, is DirtPathBlock, Blocks.DIRT, Blocks.COARSE_DIRT, is RootedDirtBlock -> {
                            if (HoeItem.canTillFarmland(ItemUsageContext(world, player, hand, stack, target as BlockHitResult)) && item is HoeItem) {
                                interactions["key.use"] = I18n.translate("gui.controls.till")
                            }
                        }
                        is LeverBlock, is AbstractButtonBlock -> {
                            interactions["key.use"] = I18n.translate("gui.controls.toggle")
                        }
                        is BedBlock -> {
                            interactions["key.use"] = I18n.translate("gui.controls.sleep")
                        }
                        is CampfireBlock -> {
                            val blockEntity = world.getBlockEntity(BlockPos.fromPosition(target.pos))
                            if (blockEntity is CampfireBlockEntity) {
                                val optional: Optional<CampfireCookingRecipe> = blockEntity.getRecipeFor(stack)
                                if (optional.isPresent) {
                                    interactions["key.use"] = I18n.translate("gui.controls.block.cook", itemName)
                                }
                            }
                        }
                        is ComposterBlock -> {
                            val i = state.get(ComposterBlock.LEVEL)
                            if (i < 8 && ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(item)) {
                                interactions["key.use"] = I18n.translate("gui.controls.block.compost", itemName)
                            }
                        }
                        is JukeboxBlock -> {
                            val blockEntity = world.getBlockEntity(BlockPos.fromPosition(target.pos))
                            if (blockEntity is JukeboxBlockEntity) {
                                if (state.get(JukeboxBlock.HAS_RECORD)) {
                                    interactions["key.use"] = I18n.translate("gui.controls.block.remove", "disk")
                                } else {
                                    if (item is MusicDiscItem) {
                                        interactions["key.use"] = I18n.translate("gui.controls.block.play", itemName)
                                    }
                                }
                            }
                        }
                        is NoteBlock -> {
                            interactions["key.use"] = I18n.translate("gui.controls.block.play", "note")
                        }
                        is CauldronBlock -> {
                            when (item) {
                                is BucketItem -> {
                                    if (item.fluid != Fluids.EMPTY) {
                                        interactions["key.use"] = I18n.translate("gui.controls.block.fill", item.fluid.defaultState.blockState.block.name.string)
                                    }
                                }
                                is PowderSnowBucketItem -> {
                                    interactions["key.use"] = I18n.translate("gui.controls.block.fill", Blocks.POWDER_SNOW.name.string)
                                }
                                is PotionItem -> {
                                    if (PotionUtil.getPotion(stack) == Potions.WATER) {
                                        interactions["key.use"] = I18n.translate("gui.controls.block.fill", itemName)
                                    }
                                }
                            }
                        }
                        is LavaCauldronBlock -> {
                            when (item) {
                                is BucketItem -> {
                                    if (item.fluid == Fluids.EMPTY) {
                                        interactions["key.use"] = I18n.translate("gui.controls.block.empty")
                                    }
                                }
                            }
                        }
                        is LeveledCauldronBlock -> {
                            if (state.get(LeveledCauldronBlock.LEVEL) in 1..2) {
                                when (item) {
                                    is PotionItem -> {
                                        if (PotionUtil.getPotion(stack) == Potions.WATER) {
                                            interactions["key.use"] = I18n.translate("gui.controls.block.fill", itemName)
                                        }
                                    }
                                }
                            } else if (state.get(LeveledCauldronBlock.LEVEL) > 0) {
                                when (item) {
                                    is BucketItem -> {
                                        if (item.fluid == Fluids.EMPTY) {
                                            interactions["key.use"] = I18n.translate("gui.controls.block.empty")
                                        }
                                    }
                                    is BlockItem -> {
                                        when (Block.getBlockFromItem(item)) {
                                            is ShulkerBoxBlock -> interactions["key.use"] = I18n.translate("gui.controls.block.clean")
                                            is BannerBlock -> if (BannerBlockEntity.getPatternCount(stack) > 0)
                                                interactions["key.use"] = I18n.translate("gui.controls.block.clean")
                                        }
                                    }
                                    is DyeableItem -> interactions["key.use"] = I18n.translate("gui.controls.block.clean")
                                }
                            }
                        }
                        is BellBlock -> {
                            interactions["key.use"] = I18n.translate("gui.controls.block.ring")
                        }
                        is AbstractRailBlock -> {
                            if (item is MinecartItem) {
                                interactions["key.use"] = I18n.translate("gui.controls.block.place")
                            }
                        }
                    }
                }
                else -> {}
            }
        }

        this.client.targetedEntity?.let { target ->
            interactions["key.attack"] = I18n.translate("gui.controls.entity.attack")

            if (player.isCreativeLevelTwoOp) {
                interactions["key.pickItem"] = I18n.translate("gui.controls.pickItem")
            }

            when (item) {
                is RangedWeaponItem -> {
                    interactions["key.use"] = I18n.translate("gui.controls.shoot")
                }
            }

            when (target) {
                is PigEntity -> {
                    if (target.isSaddled) {
                        interactions["key.use"] = I18n.translate("gui.controls.entity.mount")
                    }
                }
                is SheepEntity -> {
                    if (item == Items.SHEARS) {
                        interactions["key.use"] = I18n.translate("gui.controls.entity.shear")
                    }
                }
                is CowEntity -> {
                    if (item == Items.BUCKET) {
                        interactions["key.use"] = I18n.translate("gui.controls.entity.milk")
                    }
                }
                is MerchantEntity -> {
                    interactions["key.use"] = I18n.translate("gui.controls.entity.trade")
                }
                is ItemFrameEntity -> {
                    if (target.heldItemStack.isEmpty) {
                        interactions["key.use"] = "Store $itemName"
                    } else {
                        interactions["key.attack"] = "Remove ${target.heldItemStack.name.string}"
                        interactions["key.use"] = "Rotate ${target.heldItemStack.name.string}"
                    }
                }
            }
        }

        if (!stack.isEmpty || !offStack.isEmpty) {
            interactions["key.swapOffhand"] = I18n.translate("gui.controls.swapOffhand", stack.name.string, offStack.name.string)
        }

        if (!stack.isEmpty) {
            interactions["key.drop"] = I18n.translate("gui.controls.drop", stack.name.string)
        }

        drawCategory(graphics, textRenderer, interactions, EAST, SOUTH)
    }

    private fun drawCategory(graphics: GuiGraphics, textRenderer: TextRenderer, category: Map<String, String>, horizontalAnchor: HorizontalTextAnchor, verticalAnchor: VerticalTextAnchor, layoutDirection: LayoutDirection = LayoutDirection.VERTICAL, textKind: TextKind = TextKind.BINDING) {
        for ((i, entry) in category.entries.withIndex()) {
            val bind = KeyBind.KEY_BINDS[entry.key]

            if (bind != null) {
                // TODO: add graphical button hints
                val text = when (textKind) {
                    TextKind.BINDING -> "(${if (bind.isUnbound) I18n.translate("gui.controls.unbound") else bind.keyName?.string}) ${entry.value}"
                    TextKind.LITERAL -> entry.value
                }

                val x = when (horizontalAnchor) {
                    WEST -> padding
                    EAST -> graphics.scaledWindowWidth - textRenderer.getWidth(text) - padding
                }

                val y = when (verticalAnchor) {
                    NORTH -> padding + textRenderer.fontHeight * (if (layoutDirection == LayoutDirection.VERTICAL) i else 1)
                    SOUTH -> graphics.scaledWindowHeight - textRenderer.fontHeight.toFloat() * (if (layoutDirection == LayoutDirection.VERTICAL) category.size.toFloat() else 1.3f) + textRenderer.fontHeight * (if (layoutDirection == LayoutDirection.VERTICAL) i else 1) - padding
                }.toInt()

                graphics.drawShadowedText(
                    textRenderer,
                    text,
                    x,
                    y,
                    when {
                        bind.isPressed -> SomftCraftClient.CONFIG.hudConfig.controlsHUDConfig.pressedColour
                        bind.isUnbound -> SomftCraftClient.CONFIG.hudConfig.controlsHUDConfig.unboundColour
                        else -> SomftCraftClient.CONFIG.hudConfig.controlsHUDConfig.colour
                    }
                )
            }
        }
    }
}
