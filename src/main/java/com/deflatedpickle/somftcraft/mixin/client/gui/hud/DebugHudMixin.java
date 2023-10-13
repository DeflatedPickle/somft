/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.mixin.client.gui.hud;

import static net.minecraft.client.gui.hud.DebugHud.getBiomeString;
import static net.minecraft.client.gui.hud.DebugHud.toMiB;

import com.deflatedpickle.somftcraft.Impl;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlDebugInfo;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.LightType;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("ALL")
@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
  @Shadow @Final private MinecraftClient client;

  @Shadow
  protected abstract World getWorld();

  @Shadow
  @Nullable
  protected abstract WorldChunk getChunk();

  @Shadow @Final private DebugHud.AllocationRateCalculator allocationRateCalculator;

  @Shadow private HitResult blockHit;

  @Shadow private HitResult fluidHit;

  @ModifyVariable(
      method = "getLeftText",
      at =
          @At(
              value = "INVOKE_ASSIGN",
              target =
                  "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
              ordinal = 0))
  private String translateIntegratedServer(String s) {
    IntegratedServer integratedServer = this.client.getServer();
    ClientConnection clientConnection = this.client.getNetworkHandler().getConnection();
    float f = clientConnection.getAveragePacketsSent();
    float g = clientConnection.getAveragePacketsReceived();

    return I18n.translate("gui.debug.integrated_server", integratedServer.getTickTime(), f, g);
  }

  @ModifyVariable(
      method = "getLeftText",
      at =
          @At(
              value = "INVOKE_ASSIGN",
              target =
                  "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
              ordinal = 1))
  private String translateDedicatedServer(String s) {
    IntegratedServer integratedServer = this.client.getServer();
    ClientConnection clientConnection = this.client.getNetworkHandler().getConnection();
    float f = clientConnection.getAveragePacketsSent();
    float g = clientConnection.getAveragePacketsReceived();

    return I18n.translate("gui.debug.dedicated_server", this.client.player.getServerBrand(), f, g);
  }

  @ModifyVariable(
      method = "getLeftText",
      at =
          @At(
              value = "INVOKE_ASSIGN",
              target =
                  "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
              ordinal = 2))
  private String translateChunkRelative(String s) {
    BlockPos blockPos = this.client.getCameraEntity().getBlockPos();
    return I18n.translate(
        "gui.debug.chunk_relative",
        blockPos.getX() & 15,
        blockPos.getY() & 15,
        blockPos.getZ() & 15);
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 3))
  public boolean hideXYZ(List instance, Object e) {
    return Impl.INSTANCE.translateAndHideDebugText(
        instance,
        I18n.translate(
            "gui.debug.xyz",
            this.client.getCameraEntity().getX(),
            this.client.getCameraEntity().getY(),
            this.client.getCameraEntity().getZ()));
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 4))
  public boolean hideBlock(List instance, Object e) {
    BlockPos blockPos = this.client.getCameraEntity().getBlockPos();
    return Impl.INSTANCE.translateAndHideDebugText(
        instance,
        I18n.translate(
            "gui.debug.block",
            blockPos.getX(),
            blockPos.getY(),
            blockPos.getZ(),
            blockPos.getX() & 15,
            blockPos.getY() & 15,
            blockPos.getZ() & 15));
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 5))
  public boolean hideChunk(List instance, Object e) {
    BlockPos blockPos = this.client.getCameraEntity().getBlockPos();
    ChunkPos chunkPos = new ChunkPos(blockPos);
    return Impl.INSTANCE.translateAndHideDebugText(
        instance,
        I18n.translate(
            "gui.debug.chunk",
            chunkPos.x,
            ChunkSectionPos.getSectionCoord(blockPos.getY()),
            chunkPos.z,
            chunkPos.getRegionRelativeX(),
            chunkPos.getRegionRelativeZ(),
            chunkPos.getRegionX(),
            chunkPos.getRegionZ()));
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 6))
  public boolean hideFacing(List instance, Object e) {
    Entity entity = this.client.getCameraEntity();
    Direction direction = entity.getHorizontalFacing();

    String facing =
        switch (direction) {
          case NORTH -> I18n.translate("gui.debug.direction.north");
          case SOUTH -> I18n.translate("gui.debug.direction.south");
          case WEST -> I18n.translate("gui.debug.direction.west");
          case EAST -> I18n.translate("gui.debug.direction.east");
          default -> I18n.translate("gui.debug.direction.default");
        };

    return Impl.INSTANCE.translateAndHideDebugText(
        instance,
        I18n.translate(
            "gui.debug.facing",
            direction,
            facing,
            MathHelper.wrapDegrees(entity.getYaw()),
            MathHelper.wrapDegrees(entity.getPitch())));
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 7))
  public boolean translateEmptyChunk(List instance, Object e) {
    return instance.add(I18n.translate("gui.debug.empty_chunk"));
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 8))
  public boolean hideLight(List instance, Object e) {
    BlockPos blockPos = this.client.getCameraEntity().getBlockPos();
    int i = this.client.world.getChunkManager().getLightingProvider().getLight(blockPos, 0);
    int j = this.client.world.getLightLevel(LightType.SKY, blockPos);
    int k = this.client.world.getLightLevel(LightType.BLOCK, blockPos);
    return Impl.INSTANCE.translateAndHideDebugText(
        instance, I18n.translate("gui.debug.client_light", i, j, k));
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 11))
  public boolean hideBiome(List instance, Object e) {
    BlockPos blockPos = this.client.getCameraEntity().getBlockPos();
    return Impl.INSTANCE.translateAndHideDebugText(
        instance,
        I18n.translate("gui.debug.biome", getBiomeString(this.client.world.getBiome(blockPos))));
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 12))
  public boolean translateLocalDifficulty(List instance, Object e) {
    World world = getWorld();
    long l = 0L;
    float h = 0.0F;

    WorldChunk worldChunk2 = getChunk();
    if (worldChunk2 != null) {
      h = world.getMoonSize();
      l = worldChunk2.getInhabitedTime();
    }

    LocalDifficulty localDifficulty =
        new LocalDifficulty(world.getDifficulty(), world.getTimeOfDay(), l, h);
    return instance.add(
        I18n.translate(
            "gui.debug.local_difficulty",
            localDifficulty.getLocalDifficulty(),
            localDifficulty.getClampedLocalDifficulty(),
            this.client.world.getTimeOfDay() / 24000L));
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 13))
  public boolean translateBlending(List instance, Object e) {
    return instance.add(I18n.translate("gui.debug.blending.old"));
  }

  @Redirect(
      method = "getLeftText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 16))
  public boolean translateShader(List instance, Object e) {
    ShaderEffect shaderEffect = this.client.gameRenderer.getShader();
    return instance.add(I18n.translate("gui.debug.shader", shaderEffect));
  }

  // TODO: add a translation for SoundEngine#getDebugString

  @Redirect(
      method = "getRightText",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lcom/google/common/collect/Lists;newArrayList([Ljava/lang/Object;)Ljava/util/ArrayList;",
              opcode = Opcodes.PUTFIELD))
  private ArrayList<String> translateRightText(Object[] instance) {
    long l = Runtime.getRuntime().maxMemory();
    long m = Runtime.getRuntime().totalMemory();
    long n = Runtime.getRuntime().freeMemory();
    long o = m - n;

    return Lists.newArrayList(
        I18n.translate(
            "gui.debug.java", System.getProperty("java.version"), this.client.is64Bit() ? 64 : 32),
        I18n.translate("gui.debug.memory", o * 100L / l, toMiB(o), toMiB(l)),
        I18n.translate("gui.debug.memory.rate", toMiB(this.allocationRateCalculator.get(o))),
        I18n.translate("gui.debug.memory.allocated", m * 100L / l, toMiB(m)),
        "",
        I18n.translate("gui.debug.cpu", GlDebugInfo.getCpuInfo()),
        "",
        I18n.translate(
            "gui.debug.display",
            MinecraftClient.getInstance().getWindow().getFramebufferWidth(),
            MinecraftClient.getInstance().getWindow().getFramebufferHeight(),
            GlDebugInfo.getVendor()));
  }

  @Redirect(
      method = "getRightText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
  public boolean translateTargetedBlock(List instance, Object e) {
    BlockPos blockPos = ((BlockHitResult) this.blockHit).getBlockPos();
    return instance.add(
        I18n.translate(
            "gui.debug.target.block", blockPos.getX(), blockPos.getY(), blockPos.getZ()));
  }

  @Redirect(
      method = "getRightText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 5))
  public boolean translateTargetedFluid(List instance, Object e) {
    BlockPos blockPos = ((BlockHitResult) this.fluidHit).getBlockPos();
    return instance.add(
        I18n.translate(
            "gui.debug.target.fluid", blockPos.getX(), blockPos.getY(), blockPos.getZ()));
  }

  @Redirect(
      method = "getRightText",
      at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 9))
  public boolean translateTargetedEntity(List instance, Object e) {
    Entity entity = this.client.targetedEntity;
    return instance.add(I18n.translate("gui.debug.target.entity"));
  }

  @Inject(
      method = "getRightText",
      at =
          @At(
              value = "INVOKE",
              target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
              ordinal = 10,
              shift = At.Shift.AFTER),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION)
  public void addEntityProperties(
      CallbackInfoReturnable ci, long l, long m, long n, long o, List<String> list, Entity entity) {
    list.add("Age: " + String.valueOf(entity.age));
    list.add("Fire Ticks: " + String.valueOf(entity.getFireTicks()));
    list.add("Touching Water: " + String.valueOf(entity.isTouchingWater()));
    list.add("Submerged In Water: " + String.valueOf(entity.isSubmergedInWater()));
    list.add("Time Until Regen: " + String.valueOf(entity.timeUntilRegen));

    // TODO: add more properties

    for (var i : entity.getDataTracker().entries.values()) {
      list.add(Impl.INSTANCE.entryToString(i));
    }
  }

  @Redirect(
      method = "getRightText",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/MinecraftClient;hasReducedDebugInfo()Z"))
  public boolean hideTarget(MinecraftClient instance) {
    return instance.hasReducedDebugInfo() || !instance.player.isCreativeLevelTwoOp();
  }
}
