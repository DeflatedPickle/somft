/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("unused", "SpellCheckingInspection", "HasPlatformType")

package com.deflatedpickle.somft

import com.deflatedpickle.somft.block.PotionCauldronBlock
import com.deflatedpickle.somft.client.gui.ingame.ArmorStandScreen
import com.deflatedpickle.somft.config.SomftCraftConfig
import com.deflatedpickle.somft.item.QuiverItem
import com.deflatedpickle.somft.screen.ArmorStandScreenHandler
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SpectralArrowItem
import net.minecraft.item.TippedArrowItem
import net.minecraft.network.PacketByteBuf
import net.minecraft.potion.PotionUtil
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Axis
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.config.QuiltConfig
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer
import org.quiltmc.qsl.networking.api.PacketSender
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking

object SomftClient : ClientModInitializer {
    val CONFIG = QuiltConfig.create("somft", "config", SomftCraftConfig::class.java)

    val tooltips = listOf(
        "gui.loadtip.digging_down",
        "gui.loadtip.blaze_snowball",
        "gui.loadtip.water_mower",
        "gui.loadtip.reset_spawn",
        "gui.loadtip.tame_wolf",
        "gui.loadtip.gather_wood",
        "gui.loadtip.snowmen_rain",
        "gui.loadtip.cat_scary",
        "gui.loadtip.tame_cat",
        "gui.loadtip.creeper_lightning",
        "gui.loadtip.spider_jockey",
        "gui.loadtip.sand_gravity",
        "gui.loadtip.digging_up",
        "gui.loadtip.obsidian",
    )

    override fun onInitializeClient(mod: ModContainer) {
        CONFIG

        // TODO: hide quiver arrows when it's empty

        ColorProviderRegistry.ITEM.register(
            { itemStack: ItemStack, i: Int ->
                if (i == 0) {
                    if (QuiverItem.getQuiverOccupancy(itemStack) > 0) {
                        val optionalArrow = QuiverItem.currentArrowStack(itemStack)
                        if (optionalArrow.isPresent) {
                            val arrowItem = optionalArrow.get()
                            when (arrowItem.item) {
                                is SpectralArrowItem -> DyeColor.YELLOW.signColor
                                is TippedArrowItem -> {
                                    val effects = PotionUtil.getPotion(arrowItem).effects
                                    if (effects.isNotEmpty()) {
                                        effects.first().effectType.color
                                    } else {
                                        DyeColor.BLUE.signColor
                                    }
                                }
                                else -> -1
                            }
                        } else -1
                    } else -1
                } else -1
            },
            QuiverItem
        )

        // TODO: tint the arrow being shot from bows

        ColorProviderRegistry.BLOCK.register(
            { blockState: BlockState, _: BlockRenderView?, _: BlockPos?, i: Int ->
                if (i == 0) {
                    val effects = blockState.get(PotionCauldronBlock.POTION).potion.effects
                    if (effects.isNotEmpty()) {
                        effects.first().effectType.color
                    } else -1
                } else -1
            },
            Somft.POTION_CAULDRON
        )

        ClientPlayNetworking.registerGlobalReceiver(
            Somft.ARMOR_STAND_GUI_PACKET_ID,
        ) { client: MinecraftClient,
            _: ClientPlayNetworkHandler,
            packetByteBuf: PacketByteBuf,
            _: PacketSender ->
            val syncId = packetByteBuf.readVarInt()
            val armorStandId = packetByteBuf.readVarInt()

            client.execute {
                val entity = client.world!!.getEntityById(armorStandId)!!
                if (entity is ArmorStandEntity) {
                    val clientPlayerEntity = client.player!!
                    val armorStandScreenHandler = ArmorStandScreenHandler(syncId, clientPlayerEntity, entity)
                    clientPlayerEntity.currentScreenHandler = armorStandScreenHandler
                    client.setScreen(
                        ArmorStandScreen(armorStandScreenHandler, clientPlayerEntity, entity)
                    )
                }
            }
        }

        TrinketRendererRegistry.registerRenderer(QuiverItem) {
            stack: ItemStack,
            _: SlotReference,
            _: EntityModel<out LivingEntity>,
            matrixStack: MatrixStack,
            vertexConsumerProvider: VertexConsumerProvider,
            light: Int,
            livingEntity: LivingEntity,
            _: Float, _: Float, _: Float, _: Float, _: Float, _: Float ->
            matrixStack.push()
            matrixStack.translate(0f, 0.4f, 0.2f)
            // TODO: left handed
            // TODO: walking sway?
            matrixStack.multiply(Axis.Z_POSITIVE.rotationDegrees(180f))
            MinecraftClient.getInstance().itemRenderer.renderItem(
                livingEntity,
                stack,
                ModelTransformationMode.NONE,
                false,
                matrixStack,
                vertexConsumerProvider,
                livingEntity.world,
                light,
                LivingEntityRenderer.getOverlay(livingEntity, 0f),
                livingEntity.id
            )
            matrixStack.pop()
        }
    }
}
