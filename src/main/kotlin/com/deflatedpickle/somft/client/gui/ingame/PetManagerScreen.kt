/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.client.gui.ingame

import com.deflatedpickle.somft.api.HasPets
import com.deflatedpickle.somft.api.PetLogic
import com.deflatedpickle.somft.screen.PetManagerScreenHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.gui.screen.ingame.MerchantScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

// TODO: low health warning
// TODO: make it scrollable
// FIXME: occasionally crashes when opened
class PetManagerScreen(
    val player: PlayerEntity
) : AbstractInventoryScreen<PetManagerScreenHandler>(
    PetManagerScreenHandler(69, player.inventory),
    player.inventory,
    Text.translatable("gui.pet_manager")
) {
    companion object {
        val BACKGROUND_TEXTURE = Identifier("somftcraft", "textures/gui/container/pet_manager.png")
    }

    var selectedIndex = 0
    val pets = mutableListOf<ButtonWidget>()
    var indexStartOffset = 0
    var scrolling = false

    val padding = 3

    val petStayWidget = PetStayWidget(
        x + 82 + 49 + padding,
        y + 10 + 16,
        false
    )

    val petAttackWidget = PetAttackWidget(
        x + 82 + 49 + padding + 16 + padding,
        y + 10 + 16,
        false
    )

    override fun init() {
        super.init()
        var k: Int = y + 8

        val pets = (player as HasPets).`somft$getPets`()

        for ((i, l) in (pets.get("pets") as NbtList).withIndex()) {
            val selectedPet = (pets.get("pets") as NbtList).get(i) as NbtCompound
            val entity = player.world.entityLookup.get(selectedPet.getUuid("id")) as LivingEntity

            this.pets.add(
                addDrawableChild(
                    IndexedButton(x + 8, k, i, entity.name) {
                        this.selectedIndex = (it as IndexedButton).index + this.indexStartOffset

                        petStayWidget.checked = when ((entity as PetLogic).`somft$getMovementLogic`()) {
                            PetLogic.Movement.WANDER, PetLogic.Movement.FOLLOW, PetLogic.Movement.FOLLOW_IN_RANGE, PetLogic.Movement.FOLLOW_DISTANCED -> false
                            PetLogic.Movement.STAY -> true
                        }

                        petAttackWidget.checked = when (entity.`somft$getAttackLogic`()) {
                            PetLogic.Attack.AVOID -> false
                            PetLogic.Attack.GUARD, PetLogic.Attack.ATTACK -> true
                        }
                    }
                )
            )
            k += 20
        }

        addDrawableChild(
            PetCallWidget(
                x + 82 + 49 + padding,
                y + padding
            ) {
                MinecraftClient.getInstance().let { client ->
                    client.player?.let { player ->
                        val pets = (player as HasPets).`somft$getPets`()
                        val selectedPet = (pets.get("pets") as NbtList).get(selectedIndex) as NbtCompound
                        val entity = player.world.entityLookup.get(selectedPet.getUuid("id")) as LivingEntity

                        entity.teleport(player.x, player.y, player.z)
                    }
                }
            }
        )

        addDrawableChild(petStayWidget)
        addDrawableChild(petAttackWidget)

        addDrawableChild(
            PetAbandonWidget(
                x + 82 + 49 + 5,
                y + 60,
            ) {
                MinecraftClient.getInstance().let { client ->
                    client.player?.let { player ->
                        val pets = (player as HasPets).`somft$getPets`()
                        val selectedPet = (pets.get("pets") as NbtList).get(selectedIndex) as NbtCompound
                        val entity = player.world.entityLookup.get(selectedPet.getUuid("id")) as TameableEntity

                        entity.setOwner(null)
                    }
                }
            }
        )
    }

    override fun drawBackground(
        graphics: GuiGraphics,
        delta: Float,
        mouseX: Int,
        mouseY: Int
    ) {
        graphics.drawTexture(
            BACKGROUND_TEXTURE,
            x, y, 0,
            0f, 0f,
            backgroundWidth, backgroundHeight,
            backgroundWidth, backgroundHeight,
        )

        val pets = (player as HasPets).`somft$getPets`()
        val selectedPet = (pets.get("pets") as NbtList).get(selectedIndex) as NbtCompound
        val entity = player.world.entityLookup.get(selectedPet.getUuid("id")) as LivingEntity

        InventoryScreen.drawEntity(
            graphics,
            x + 82 + 49 / 2,
            y + 75,
            30,
            (x + 82 + 49 / 2).toFloat() - mouseX,
            (y + 75 - 50).toFloat() - mouseY,
            entity
        )
    }

    private fun renderScrollbar(
        graphics: GuiGraphics,
        x: Int,
        y: Int,
    ) {
        val i = pets.size + 1 - 7
        if (i > 1) {
            val j = 139 - (27 + (i - 1) * 139 / i)
            val k = 1 + j / i + 139 / i
            var m = 113.coerceAtMost(this.indexStartOffset * k)
            if (this.indexStartOffset == i - 1) {
                m = 113
            }
            graphics.drawTexture(MerchantScreen.TEXTURE, x + 69, y + 8 + m, 0, 0.0f, 199.0f, 6, 27, 512, 256)
        } else {
            graphics.drawTexture(MerchantScreen.TEXTURE, x + 69, y + 8, 0, 6.0f, 199.0f, 6, 27, 512, 256)
        }
    }

    override fun render(
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        super.render(graphics, mouseX, mouseY, delta)

        if (this.pets.isNotEmpty()) {
            this.renderScrollbar(graphics, x, y)
        }

        this.drawMouseoverTooltip(graphics, x, y)
    }

    private fun canScroll(listSize: Int) = listSize > 7

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        amount: Double
    ): Boolean {
        val i: Int = pets.size
        if (this.canScroll(i)) {
            val j = i - 7
            indexStartOffset = MathHelper.clamp((indexStartOffset.toDouble() - amount).toInt(), 0, j)
        }
        return true
    }

    override fun mouseDragged(
        mouseX: Double,
        mouseY: Double,
        button: Int,
        deltaX: Double,
        deltaY: Double
    ): Boolean {
        val i: Int = pets.size
        return if (scrolling) {
            val j = y + 18
            val k = j + 139
            val l = i - 7
            var f = (mouseY.toFloat() - j.toFloat() - 13.5f) / ((k - j).toFloat() - 27.0f)
            f = f * l.toFloat() + 0.5f
            indexStartOffset = MathHelper.clamp(f.toInt(), 0, l)
            true
        } else {
            super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
        }
    }

    override fun mouseClicked(
        mouseX: Double,
        mouseY: Double,
        button: Int
    ): Boolean {
        scrolling = false
        val i = (width - backgroundWidth) / 2
        val j = (height - backgroundHeight) / 2
        if (this.canScroll(pets.size) && mouseX > (i + 94).toDouble() && mouseX < (i + 94 + 6).toDouble() && mouseY > (j + 18).toDouble() && mouseY <= (j + 18 + 139 + 1).toDouble()) {
            scrolling = true
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    class IndexedButton(
        x: Int,
        y: Int,
        val index: Int,
        text: Text,
        pressAction: PressAction,
    ) : ButtonWidget(
        x, y,
        60, 20,
        text,
        pressAction,
        DEFAULT_NARRATION
    )
}
