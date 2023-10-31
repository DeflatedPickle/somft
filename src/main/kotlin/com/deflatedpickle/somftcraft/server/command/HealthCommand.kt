/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.server.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.command.EntitySelector
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object HealthCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<ServerCommandSource>("health")
                .requires { it.hasPermissionLevel(2) }
                .then(
                    LiteralArgumentBuilder.literal<ServerCommandSource>("add")
                        .then(
                            RequiredArgumentBuilder.argument<ServerCommandSource, EntitySelector>("targets", EntityArgumentType.players())
                                .then(
                                    RequiredArgumentBuilder.argument<ServerCommandSource, Float>("amount", FloatArgumentType.floatArg())
                                        .executes {
                                            val targets = EntityArgumentType.getPlayers(it, "targets")
                                            val amount = FloatArgumentType.getFloat(it, "amount")

                                            for (i in targets) {
                                                i.heal(amount)
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
                                    RequiredArgumentBuilder.argument<ServerCommandSource, Float>("amount", FloatArgumentType.floatArg())
                                        .executes {
                                            val targets = EntityArgumentType.getPlayers(it, "targets")
                                            val amount = FloatArgumentType.getFloat(it, "amount")

                                            for (i in targets) {
                                                i.health = amount
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
                                    RequiredArgumentBuilder.argument<ServerCommandSource, Float>("amount", FloatArgumentType.floatArg())
                                        .executes {
                                            val targets = EntityArgumentType.getPlayer(it, "targets")
                                            val amount = FloatArgumentType.getFloat(it, "amount")

                                            it.source.sendFeedback(
                                                { Text.translatable("commands.health.query", targets.displayName, amount) },
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
