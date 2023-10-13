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
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.option.KeyBind
import net.minecraft.client.resource.language.I18n
import net.minecraft.entity.passive.CowEntity
import net.minecraft.entity.passive.MerchantEntity
import net.minecraft.entity.passive.PigEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.item.HoeItem
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.Items
import net.minecraft.item.RangedWeaponItem
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos

class ControlsHud(
    val client: MinecraftClient
) {
    val padding = 5

    val colour = DyeColor.WHITE.signColor
    val pressedColour = DyeColor.LIGHT_GRAY.signColor
    val unboundColour = DyeColor.GRAY.signColor

    fun render(graphics: GuiGraphics) {
        if (this.client.options.debugEnabled || !SomftCraftClient.CONFIG.hudConfig.showControls) return

        val textRenderer = this.client.textRenderer
        val world = this.client.world ?: return
        val player = this.client.player ?: return
        val hand = player.activeHand
        val stack = player.mainHandStack
        val offStack = player.offHandStack
        val item = stack.item

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

        if (!stack.isEmpty || !offStack.isEmpty) {
            interactions["key.swapOffhand"] = I18n.translate("gui.controls.swapOffhand", stack.name.string, offStack.name.string)
        }

        if (!stack.isEmpty) {
            interactions["key.drop"] = I18n.translate("gui.controls.drop", stack.name.string)
        }

        this.client.crosshairTarget?.let { target ->
            if (target.type == HitResult.Type.BLOCK) {
                val state = world.getBlockState(BlockPos.fromPosition(target.pos))
                val block = state.block

                if (state.isAir) return

                val name = block.name.string

                interactions["key.attack"] = I18n.translate("gui.controls.block.attack", name)

                if (player.isCreativeLevelTwoOp) {
                    interactions["key.pickItem"] = I18n.translate("gui.controls.pickItem", name)
                }

                when (block) {
                    Blocks.CRAFTING_TABLE, Blocks.FURNACE, Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL, Blocks.CHEST -> {
                        interactions["key.use"] = I18n.translate("gui.controls.open", name)
                    }
                    Blocks.GRASS_BLOCK, Blocks.DIRT_PATH, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT -> {
                        if (HoeItem.canTillFarmland(ItemUsageContext(world, player, hand, stack, target as BlockHitResult)) && item is HoeItem) {
                            interactions["key.use"] = I18n.translate("gui.controls.till", name)
                        }
                    }
                }
            }
        }

        this.client.targetedEntity?.let { target ->
            val name = target.name.string

            interactions["key.attack"] = I18n.translate("gui.controls.entity.attack", name)

            if (player.isCreativeLevelTwoOp) {
                interactions["key.pickItem"] = I18n.translate("gui.controls.pickItem", name)
            }

            when (item) {
                is RangedWeaponItem -> {
                    interactions["key.use"] = I18n.translate("gui.controls.shoot", name)
                }
            }

            when (val entity = this.client.targetedEntity) {
                is PigEntity -> {
                    if (entity.isSaddled) {
                        interactions["key.use"] = I18n.translate("gui.controls.entity.mount", name)
                    }
                }
                is SheepEntity -> {
                    if (item == Items.SHEARS) {
                        interactions["key.use"] = I18n.translate("gui.controls.entity.shear", name)
                    }
                }
                is CowEntity -> {
                    if (item == Items.BUCKET) {
                        interactions["key.use"] = I18n.translate("gui.controls.entity.milk", name)
                    }
                }
                is MerchantEntity -> {
                    interactions["key.use"] = I18n.translate("gui.controls.entity.trade", name)
                }
            }
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
                        bind.isPressed -> pressedColour
                        bind.isUnbound -> unboundColour
                        else -> colour
                    }
                )
            }
        }
    }
}
