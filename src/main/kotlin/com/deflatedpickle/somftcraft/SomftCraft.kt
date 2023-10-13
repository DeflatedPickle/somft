/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

@file:Suppress("ClassName", "SpellCheckingInspection", "MemberVisibilityCanBePrivate")

package com.deflatedpickle.somftcraft

import com.deflatedpickle.somftcraft.block.dispenser.HorseArmorDispenserBehavior
import com.deflatedpickle.somftcraft.block.dispenser.TorchDispenserBehavior
import com.deflatedpickle.somftcraft.item.EmptyInkSacItem
import com.deflatedpickle.somftcraft.item.HorseArmorItemExt
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.minecraft.block.dispenser.DispenserBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTables
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.world.GameRules
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

object SomftCraft : ModInitializer {
    val CHAINMAIL_HORSE_ARMOUR = HorseArmorItemExt(6, "chainmail", Item.Settings().maxCount(1))
    val NETHERITE_HORSE_ARMOUR = HorseArmorItemExt(12, "netherite", Item.Settings().maxCount(1))

    val DO_BLOCK_FIRE_GRIEF = GameRuleRegistry.register("doBlockFireGrief", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true))
    val DO_MOB_FIRE_GRIEF = GameRuleRegistry.register("doMobFireGrief", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true))

    override fun onInitialize(mod: ModContainer) {
        Registry.register(Registries.ITEM, Identifier(mod.metadata().id(), "empty_ink_sac"), EmptyInkSacItem)
        Registry.register(Registries.ITEM, Identifier(mod.metadata().id(), "chainmail_horse_armor"), CHAINMAIL_HORSE_ARMOUR)
        Registry.register(Registries.ITEM, Identifier(mod.metadata().id(), "netherite_horse_armor"), NETHERITE_HORSE_ARMOUR)

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register { entries ->
            entries.addItem(EmptyInkSacItem)
        }

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register { entries ->
            entries.addItem(CHAINMAIL_HORSE_ARMOUR)
            entries.addItem(NETHERITE_HORSE_ARMOUR)
        }

        LootTableEvents.MODIFY.register { resourceManager, lootManager, id, tableBuilder, source ->
            if (source.isBuiltin) {
                val poolBuilder: LootPool.Builder = LootPool.builder()

                when (id) {
                    LootTables.ANCIENT_CITY_CHEST, LootTables.NETHER_BRIDGE_CHEST, LootTables.STRONGHOLD_CORRIDOR_CHEST ->
                        poolBuilder
                            .with(ItemEntry.builder(CHAINMAIL_HORSE_ARMOUR).weight(1))
                            .with(ItemEntry.builder(NETHERITE_HORSE_ARMOUR).weight(1))
                    LootTables.DESERT_PYRAMID_CHEST ->
                        poolBuilder
                            .with(ItemEntry.builder(CHAINMAIL_HORSE_ARMOUR).weight(12))
                    LootTables.END_CITY_TREASURE_CHEST ->
                        poolBuilder
                            .with(ItemEntry.builder(CHAINMAIL_HORSE_ARMOUR))
                            .with(ItemEntry.builder(NETHERITE_HORSE_ARMOUR))
                    LootTables.JUNGLE_TEMPLE_CHEST ->
                        poolBuilder
                            .with(ItemEntry.builder(CHAINMAIL_HORSE_ARMOUR))
                    LootTables.RUINED_PORTAL_CHEST ->
                        poolBuilder
                            .with(ItemEntry.builder(CHAINMAIL_HORSE_ARMOUR).weight(2))
                    LootTables.WOODLAND_MANSION_CHEST ->
                        poolBuilder
                            .with(ItemEntry.builder(CHAINMAIL_HORSE_ARMOUR).weight(5))
                }

                tableBuilder.pool(poolBuilder)
            }
        }

        DispenserBlock.registerBehavior(CHAINMAIL_HORSE_ARMOUR, HorseArmorDispenserBehavior)
        DispenserBlock.registerBehavior(NETHERITE_HORSE_ARMOUR, HorseArmorDispenserBehavior)
        DispenserBlock.registerBehavior(Items.TORCH, TorchDispenserBehavior)
    }
}
