/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.state;

import java.util.regex.Pattern;
import net.minecraft.state.StateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"UnusedMixin", "SpellCheckingInspection"})
@Mixin(StateManager.class)
public class StateManagerMixin {
  @Redirect(
      method = "<clinit>",
      at =
          @At(
              value = "FIELD",
              target =
                  "Lnet/minecraft/state/StateManager;VALID_NAME_PATTERN:Ljava/util/regex/Pattern;"))
  private static void somft$VALID_NAME_PATTERN(Pattern value) {
    StateManager.VALID_NAME_PATTERN = Pattern.compile("^[a-z0-9_:]+$");
  }
}
