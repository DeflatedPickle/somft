/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.mixin.entity.passive;

import com.deflatedpickle.somft.Impl;
import com.deflatedpickle.somft.api.Breedable;
import com.deflatedpickle.somft.api.DoesAge;
import com.deflatedpickle.somft.api.Milkable;
import com.deflatedpickle.somft.entity.ai.goal.FollowParentSquidGoal;
import com.deflatedpickle.somft.entity.ai.goal.SquidMateGoal;
import com.deflatedpickle.somft.item.EmptyInkSacItem;
import java.util.UUID;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameRules;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"UnusedMixin", "WrongEntityDataParameterClass", "SpellCheckingInspection"})
@Mixin(SquidEntity.class)
public abstract class SquidEntityMixin extends WaterCreatureEntity
    implements Breedable, DoesAge, Milkable {
  @Unique
  private static final TrackedData<Boolean> CHILD =
      DataTracker.registerData(SquidEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

  ;

  @Unique
  private static final TrackedData<Integer> MILK_TICKS =
      DataTracker.registerData(SquidEntity.class, TrackedDataHandlerRegistry.INTEGER);

  @Unique private static final int MILK_TICKS_MAX = 24000 / 6;
  @Unique private static final String NBT_KEY = "Ink";
  @Unique protected int breedingAge;
  @Unique protected int forcedAge;
  @Unique protected int happyTicksRemaining;
  @Unique private int loveTicks;
  @Unique @Nullable private UUID lovingPlayer;

  protected SquidEntityMixin(EntityType<? extends WaterCreatureEntity> entityType, World world) {
    super(entityType, world);
  }

  @Override
  public EntityData initialize(
      ServerWorldAccess world,
      LocalDifficulty difficulty,
      SpawnReason spawnReason,
      @Nullable EntityData entityData,
      @Nullable NbtCompound entityNbt) {
    if (entityData == null) {
      entityData = new PassiveEntity.PassiveData(true);
    }

    PassiveEntity.PassiveData passiveData = (PassiveEntity.PassiveData) entityData;
    if (passiveData.canSpawnBaby()
        && passiveData.getSpawnedCount() > 0
        && world.getRandom().nextFloat() <= passiveData.getBabyChance()) {
      this.setBreedingAge(-24000);
    }

    passiveData.countSpawned();
    return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
  }

  @ModifyArg(
      method = "initGoals",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V"),
      index = 0)
  public int priorityIncrement(int priority) {
    return priority + 2;
  }

  @Inject(method = "initGoals", at = @At("TAIL"))
  public void onInitGoals(CallbackInfo ci) {
    this.goalSelector.add(0, new SquidMateGoal((SquidEntity) (Object) this, 1.0));
    this.goalSelector.add(1, new FollowParentSquidGoal((SquidEntity) (Object) this, 1.1));
  }

  @Override
  public void somft$onGrowUp() {}

  public boolean somft$isBaby() {
    return this.getBreedingAge() < 0;
  }

  @Override
  public boolean somft$isAdult() {
    return this.getBreedingAge() >= 0;
  }

  @Override
  public void setBaby(boolean baby) {
    this.setBreedingAge(baby ? -24000 : 0);
  }

  @Unique
  private static int m_eyazmlyb(int i) {
    return (int) ((float) (i / 20) * 0.1F);
  }

  @Unique
  public SquidEntity createChild(ServerWorld world, SquidEntity entity) {
    if (entity instanceof GlowSquidEntity) {
      return EntityType.GLOW_SQUID.create(world);
    } else {
      return EntityType.SQUID.create(world);
    }
  }

  protected void initDataTracker() {
    super.initDataTracker();
    this.dataTracker.startTracking(CHILD, false);
    this.dataTracker.startTracking(MILK_TICKS, MILK_TICKS_MAX);
  }

  @Unique
  public int getBreedingAge() {
    if (getWorld().isClient) {
      return this.dataTracker.get(CHILD) ? -1 : 1;
    } else {
      return this.breedingAge;
    }
  }

  @Unique
  public void setBreedingAge(int age) {
    int i = this.getBreedingAge();
    this.breedingAge = age;
    if (i < 0 && age >= 0 || i >= 0 && age < 0) {
      this.dataTracker.set(CHILD, age < 0);
      this.somft$onGrowUp();
    }
  }

  @Unique
  public void growUp(int age, boolean overGrow) {
    int i = this.getBreedingAge();
    int j = i;
    i += age * 20;
    if (i > 0) {
      i = 0;
    }

    int k = i - j;
    this.setBreedingAge(i);
    if (overGrow) {
      this.forcedAge += k;
      if (this.happyTicksRemaining == 0) {
        this.happyTicksRemaining = 40;
      }
    }

    if (this.getBreedingAge() == 0) {
      this.setBreedingAge(this.forcedAge);
    }
  }

  @Override
  public void writeCustomDataToNbt(NbtCompound nbt) {
    super.writeCustomDataToNbt(nbt);
    nbt.putInt("Age", this.getBreedingAge());
    nbt.putInt("ForcedAge", this.forcedAge);

    nbt.putInt("InLove", this.loveTicks);
    if (this.lovingPlayer != null) {
      nbt.putUuid("LoveCause", this.lovingPlayer);
    }

    nbt.putInt(NBT_KEY, this.somft$getMilkTicks());
  }

  @Override
  public void readCustomDataFromNbt(NbtCompound nbt) {
    super.readCustomDataFromNbt(nbt);
    this.setBreedingAge(nbt.getInt("Age"));
    this.forcedAge = nbt.getInt("ForcedAge");

    this.loveTicks = nbt.getInt("InLove");
    this.lovingPlayer = nbt.containsUuid("LoveCause") ? nbt.getUuid("LoveCause") : null;

    this.somft$setMilkTicks(nbt.getInt(NBT_KEY));
  }

  @Override
  public void onTrackedDataUpdate(TrackedData<?> data) {
    if (CHILD.equals(data)) {
      this.calculateDimensions();
    }

    super.onTrackedDataUpdate(data);
  }

  @Override
  protected void mobTick() {
    if (this.getBreedingAge() != 0) {
      this.loveTicks = 0;
    }

    super.mobTick();
  }

  @Inject(method = "tickMovement", at = @At("TAIL"))
  public void onTickMovement(CallbackInfo ci) {
    if (getWorld().isClient) {
      if (this.happyTicksRemaining > 0) {
        if (this.happyTicksRemaining % 4 == 0) {
          getWorld()
              .addParticle(
                  ParticleTypes.HAPPY_VILLAGER,
                  this.getParticleX(1.0),
                  this.getRandomBodyY() + 0.5,
                  this.getParticleZ(1.0),
                  0.0,
                  0.0,
                  0.0);
        }

        --this.happyTicksRemaining;
      }
    } else if (this.isAlive()) {
      int i = this.getBreedingAge();
      if (i < 0) {
        this.setBreedingAge(++i);
      } else if (i > 0) {
        this.setBreedingAge(--i);
      }
    }
  }

  @Unique
  public boolean isBreedingItem(ItemStack stack) {
    return stack.isOf(Items.COD) || stack.isOf(Items.SALMON) || stack.isOf(Items.TROPICAL_FISH);
  }

  @Override
  public ActionResult interactMob(PlayerEntity player, Hand hand) {
    ItemStack itemStack = player.getStackInHand(hand);
    if (this.isBreedingItem(itemStack)) {
      int i = this.getBreedingAge();
      if (!getWorld().isClient && i == 0 && this.canEat()) {
        this.eat(player, hand, itemStack);
        this.lovePlayer(player);
        return ActionResult.SUCCESS;
      }

      if (this.somft$isBaby()) {
        this.eat(player, hand, itemStack);
        this.growUp(m_eyazmlyb(-i), true);
        return ActionResult.success(getWorld().isClient);
      }

      if (getWorld().isClient) {
        return ActionResult.CONSUME;
      }
    } else {
      if (this.somft$getMilkTicks() > 0) {
        playAmbientSound();
        Impl.INSTANCE.spawnPlayerReactionParticles(this);
        return super.interactMob(player, hand);
      } else if (itemStack.isOf(EmptyInkSacItem.INSTANCE) && !this.somft$isBaby()) {
        player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);

        Item item;
        if ((Object) this instanceof GlowSquidEntity) {
          item = Items.GLOW_INK_SAC;
        } else {
          item = Items.INK_SAC;
        }

        ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, player, item.getDefaultStack());
        player.setStackInHand(hand, itemStack2);
        this.somft$setMilkTicks(MILK_TICKS_MAX);
        return ActionResult.success(this.getWorld().isClient);
      }
    }

    return super.interactMob(player, hand);
  }

  @Override
  public void tick() {
    super.tick();
    this.somft$setMilkTicks(Math.max(0, somft$getMilkTicks() - 1));
  }

  @Unique
  protected void eat(PlayerEntity player, Hand hand, ItemStack stack) {
    if (!player.getAbilities().creativeMode) {
      stack.decrement(1);
    }
  }

  @Unique
  public boolean canEat() {
    return this.loveTicks <= 0;
  }

  @Unique
  public void lovePlayer(@Nullable PlayerEntity player) {
    this.loveTicks = 600;
    if (player != null) {
      this.lovingPlayer = player.getUuid();
    }

    getWorld().sendEntityStatus(this, EntityStatuses.ADD_BREEDING_PARTICLES);
  }

  @Unique
  @Nullable
  public ServerPlayerEntity getLovingPlayer() {
    if (this.lovingPlayer == null) {
      return null;
    } else {
      PlayerEntity playerEntity = getWorld().getPlayerByUuid(this.lovingPlayer);
      return playerEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity) playerEntity : null;
    }
  }

  @Unique
  @Override
  public boolean somft$isInLove() {
    return this.loveTicks > 0;
  }

  @Unique
  public void resetLoveTicks() {
    this.loveTicks = 0;
  }

  @Unique
  @Override
  public boolean somft$canBreedWith(@NotNull WaterCreatureEntity other) {
    if (other == this) {
      return false;
    } else if (other.getClass() != this.getClass()) {
      return false;
    } else {
      return this.somft$isInLove() && ((SquidEntityMixin) other).somft$isInLove();
    }
  }

  @Unique
  @Override
  public void somft$breed(@NotNull ServerWorld world, SquidEntity other) {
    SquidEntity passiveEntity = this.createChild(world, other);
    if (passiveEntity != null) {
      ServerPlayerEntity serverPlayerEntity = this.getLovingPlayer();
      if (serverPlayerEntity == null
          && ((SquidEntityMixin) (Object) other).getLovingPlayer() != null) {
        serverPlayerEntity = ((SquidEntityMixin) (Object) other).getLovingPlayer();
      }

      if (serverPlayerEntity != null) {
        serverPlayerEntity.incrementStat(Stats.ANIMALS_BRED);
        // Criteria.BRED_ANIMALS.trigger(serverPlayerEntity, this, other, passiveEntity);
      }

      this.setBreedingAge(6000);
      ((SquidEntityMixin) (Object) other).setBreedingAge(6000);
      this.resetLoveTicks();
      ((SquidEntityMixin) (Object) other).resetLoveTicks();
      passiveEntity.setBaby(true);
      passiveEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
      world.spawnEntityAndPassengers(passiveEntity);
      world.sendEntityStatus(this, EntityStatuses.ADD_BREEDING_PARTICLES);
      if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
        world.spawnEntity(
            new ExperienceOrbEntity(
                world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
      }
    }
  }

  @Override
  public void handleStatus(byte status) {
    if (status == EntityStatuses.ADD_BREEDING_PARTICLES) {
      for (int i = 0; i < 7; ++i) {
        double d = this.random.nextGaussian() * 0.02;
        double e = this.random.nextGaussian() * 0.02;
        double f = this.random.nextGaussian() * 0.02;
        getWorld()
            .addParticle(
                ParticleTypes.HEART,
                this.getParticleX(1.0),
                this.getRandomBodyY() + 0.5,
                this.getParticleZ(1.0),
                d,
                e,
                f);
      }
    } else {
      super.handleStatus(status);
    }
  }

  public int somft$getMilkTicks() {
    return this.dataTracker.get(MILK_TICKS);
  }

  public void somft$setMilkTicks(int ticks) {
    this.dataTracker.set(MILK_TICKS, ticks);
  }
}
