/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.server.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.command.EntitySelector
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import kotlin.math.max
import kotlin.math.min

// TODO: support saturation and exhaustion
object HungerCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<ServerCommandSource>("hunger")
                .requires { it.hasPermissionLevel(2) }
                .then(
                    LiteralArgumentBuilder.literal<ServerCommandSource>("add")
                        .then(
                            RequiredArgumentBuilder.argument<ServerCommandSource, EntitySelector>("targets", EntityArgumentType.players())
                                .then(
                                    RequiredArgumentBuilder.argument<ServerCommandSource, Int>("amount", IntegerArgumentType.integer())
                                        .executes {
                                            val targets = EntityArgumentType.getPlayers(it, "targets")
                                            val amount = IntegerArgumentType.getInteger(it, "amount")

                                            for (i in targets) {
                                                i.hungerManager.foodLevel = min(i.hungerManager.foodLevel + amount, 20)
                                            }

                                            // TODO: broadcast message

                                            targets.size
                                        }
                                )
                        )
                )
                .then(
                    LiteralArgumentBuilder.literal<ServerCommandSource>("set")
                        .then(
                            RequiredArgumentBuilder.argument<ServerCommandSource, EntitySelector>("targets", EntityArgumentType.players())
                                .then(
                                    RequiredArgumentBuilder.argument<ServerCommandSource, Int>("amount", IntegerArgumentType.integer())
                                        .executes {
                                            val targets = EntityArgumentType.getPlayers(it, "targets")
                                            val amount = IntegerArgumentType.getInteger(it, "amount")

                                            for (i in targets) {
                                                i.hungerManager.foodLevel = max(0, amount)
                                            }

                                            // TODO: broadcast message

                                            targets.size
                                        }
                                )
                        )
                )
                .then(
                    LiteralArgumentBuilder.literal<ServerCommandSource>("query")
                        .then(
                            RequiredArgumentBuilder.argument<ServerCommandSource, EntitySelector>("targets", EntityArgumentType.player())
                                .then(
                                    RequiredArgumentBuilder.argument<ServerCommandSource, Int>("amount", IntegerArgumentType.integer())
                                        .executes {
                                            val targets = EntityArgumentType.getPlayer(it, "targets")
                                            val amount = FloatArgumentType.getFloat(it, "amount")

                                            it.source.sendFeedback(
                                                { Text.translatable("commands.hunger.food.query", targets.displayName, amount) },
                                                false
                                            )

                                            amount.toInt()
                                        }
                                )
                        )
                )
        )
    }
}
