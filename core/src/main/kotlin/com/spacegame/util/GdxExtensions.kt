package com.spacegame.util

import com.badlogic.gdx.math.Vector2
import kotlin.math.cos
import kotlin.math.sin

fun vec2FromAngle(angle: Double, magnitude: Double = 1.0): Vector2 =
    Vector2((magnitude * cos(angle)).toFloat(), (magnitude * sin(angle)).toFloat())