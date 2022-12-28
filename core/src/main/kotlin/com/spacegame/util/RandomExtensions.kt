package com.spacegame.util

import kotlin.random.Random

fun Random.nextFloat(from: Float, until: Float): Float =
    from + nextFloat() * (until - from)