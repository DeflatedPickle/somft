/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.recipe

import com.deflatedpickle.somft.Somft
import net.minecraft.block.Blocks
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.CraftingCategory
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.recipe.SpecialRecipeSerializer
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList

class DispenserRecipe(
    identifier: Identifier,
    craftingCategory: CraftingCategory
) : ShapedRecipe(
    identifier,
    "",
    craftingCategory,
    3,
    3,
    DefaultedList.copyOf(
        Ingredient.EMPTY,
        Ingredient.ofItems(Items.COBBLESTONE),
        Ingredient.ofItems(Items.COBBLESTONE),
        Ingredient.ofItems(Items.COBBLESTONE),
        Ingredient.ofItems(Items.COBBLESTONE),
        Ingredient.ofItems(Items.BOW),
        Ingredient.ofItems(Items.COBBLESTONE),
        Ingredient.ofItems(Items.COBBLESTONE),
        Ingredient.ofItems(Items.REDSTONE),
        Ingredient.ofItems(Items.COBBLESTONE)
    ),
    ItemStack(Blocks.DISPENSER)
) {
    override fun craft(
        recipeInputInventory: RecipeInputInventory,
        dynamicRegistryManager: DynamicRegistryManager
    ): ItemStack {
        val dispenser = super.craft(recipeInputInventory, dynamicRegistryManager)
        val bow = getBowStack(recipeInputInventory)
        val compound = dispenser.getOrCreateSubNbt("Bow")
        bow.writeNbt(compound)
        return dispenser
    }

    private fun getBowStack(inventory: RecipeInputInventory): ItemStack {
        for (i in 0 until inventory.size()) {
            val itemStack = inventory.getStack(i)
            if (itemStack.isOf(Items.BOW)) {
                return itemStack
            }
        }

        return ItemStack.EMPTY
    }

    override fun getSerializer(): SpecialRecipeSerializer<DispenserRecipe> =
        Somft.DISPENSER_RECIPE
}
