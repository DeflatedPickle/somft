/* Copyright (c) 2023 DeflatedPickle under the GPLv3 license */

package com.deflatedpickle.somft.api

interface TextAnchor

enum class HorizontalTextAnchor : TextAnchor {
    EAST,
    WEST,
}

enum class VerticalTextAnchor : TextAnchor {
    NORTH,
    SOUTH
}
