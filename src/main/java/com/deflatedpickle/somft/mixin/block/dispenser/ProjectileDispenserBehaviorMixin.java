/* Copyright (c) 2024 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.block.dispenser;

import com.deflatedpickle.somft.api.DispenserExt;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(ProjectileDispenserBehavior.class)
public class ProjectileDispenserBehaviorMixin {
  @Inject(
      method = "dispenseSilently",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
              shift = At.Shift.BEFORE),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void somft$dispenseSilently(
      BlockPointer pointer,
      ItemStack stack,
      CallbackInfoReturnable<ItemStack> cir,
      World world,
      Position position,
      Direction direction,
      ProjectileEntity projectileEntity) {
    if (projectileEntity instanceof PersistentProjectileEntity persistentProjectileEntity) {
      var bowStack = ((DispenserExt) pointer.getBlockEntity()).getSomft$bowStack();

      if (bowStack != null) {
        int j = EnchantmentHelper.getLevel(Enchantments.POWER, bowStack);
        if (j > 0) {
          persistentProjectileEntity.setDamage(
              persistentProjectileEntity.getDamage() + (double) j * 0.5 + 0.5);
        }

        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, bowStack);
        if (k > 0) {
          persistentProjectileEntity.setPunch(k);
        }

        if (EnchantmentHelper.getLevel(Enchantments.FLAME, bowStack) > 0) {
          persistentProjectileEntity.setOnFireFor(100);
        }
      }
    }
  }
}
