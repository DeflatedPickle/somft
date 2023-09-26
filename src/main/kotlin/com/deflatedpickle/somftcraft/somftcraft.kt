/* Copyright (c) 2023 DeflatedPickle under the MIT license */

@file:Suppress("ClassName", "SpellCheckingInspection", "MemberVisibilityCanBePrivate")

package com.deflatedpickle.somftcraft

import com.deflatedpickle.somftcraft.item.EmptyInkSacItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer


object somftcraft : ModInitializer {
    override fun onInitialize(mod: ModContainer) {
        Registry.register(Registries.ITEM, Identifier(mod.metadata().id(), "empty_ink_sac"), EmptyInkSacItem)
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register { entries ->
            entries.addItem(EmptyInkSacItem)
        }
    }
}
