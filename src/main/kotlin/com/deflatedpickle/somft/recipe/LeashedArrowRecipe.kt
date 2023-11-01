/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.recipe

import com.deflatedpickle.somft.Somft
import com.deflatedpickle.somft.item.LeashedArrow
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.CraftingCategory
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.SpecialRecipeSerializer
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier
import net.minecraft.world.World

class LeashedArrowRecipe(
    identifier: Identifier,
    craftingCategory: CraftingCategory
) : SpecialCraftingRecipe(identifier, craftingCategory) {
    override fun matches(
        inventory: RecipeInputInventory,
        world: World
    ): Boolean {
        var arrowStack = ItemStack.EMPTY
        var leashStack = ItemStack.EMPTY

        for (i in 0 until inventory.size()) {
            val loopStack = inventory.getStack(i)
            if (!loopStack.isEmpty) {
                if (loopStack.isIn(ItemTags.ARROWS) && arrowStack.isEmpty) {
                    arrowStack = loopStack
                } else if (loopStack.isOf(Items.LEAD) && leashStack.isEmpty) {
                    leashStack = loopStack
                } else {
                    return false
                }
            }
        }

        return !arrowStack.isEmpty && !leashStack.isEmpty
    }

    override fun craft(
        inventory: RecipeInputInventory,
        registryManager: DynamicRegistryManager
    ): ItemStack {
        var stack = ItemStack.EMPTY
        for (i in 0 until inventory.size()) {
            val itemStack = inventory.getStack(i)
            if (itemStack.isIn(ItemTags.ARROWS)) {
                stack = itemStack
            }
        }

        return if (!stack.isEmpty) {
            val leashedArrow = ItemStack(LeashedArrow, 1)
            LeashedArrow.setArrowStack(leashedArrow, stack)
            leashedArrow
        } else {
            ItemStack.EMPTY
        }
    }

    override fun fits(width: Int, height: Int) = width >= 2 && height >= 2

    override fun getSerializer(): SpecialRecipeSerializer<LeashedArrowRecipe> =
        Somft.LEASHED_ARROW_RECIPE_SERIALIZER
}
