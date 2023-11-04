/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.api

import com.deflatedpickle.somft.api.IconType.EMPTY
import com.deflatedpickle.somft.api.IconType.FULL
import com.deflatedpickle.somft.api.IconType.HALF

enum class IconType {
    EMPTY,
    HALF,
    FULL,
    ;
}

enum class IconSet(val backwards: Boolean) {
    HEART(false) {
        override fun getUV(type: IconType) =
            Pair(
                when (type) {
                    EMPTY -> 16
                    HALF -> 62
                    FULL -> 53
                },
                0
            )
    },
    ARMOR(false) {
        override fun getUV(type: IconType): Pair<Int, Int> =
            Pair(
                when (type) {
                    EMPTY -> 16
                    HALF -> 25
                    FULL -> 35
                },
                9
            )
    },
    HUNGER(true) {
        override fun getUV(type: IconType): Pair<Int, Int> =
            Pair(
                when (type) {
                    EMPTY -> 16
                    HALF -> 61
                    FULL -> 51
                },
                27
            )
    },
    ;
    abstract fun getUV(type: IconType): Pair<Int, Int>
}
