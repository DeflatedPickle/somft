/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somftcraft.entity.data

import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.nbt.NbtIntArray
import net.minecraft.network.PacketByteBuf

object IntArrayTrackedDataHandler : TrackedDataHandler<NbtIntArray> {
    override fun write(buf: PacketByteBuf, value: NbtIntArray) {
        buf.writeIntArray(value.intArray)
    }

    override fun read(buf: PacketByteBuf) =
        NbtIntArray(buf.readIntArray())

    override fun copy(value: NbtIntArray): NbtIntArray =
        value.copy()
}
