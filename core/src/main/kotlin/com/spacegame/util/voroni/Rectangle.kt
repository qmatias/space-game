package com.spacegame.util.voroni

class Rectangle(var left: Double = 0.0, var top: Double = 0.0, var right: Double = 0.0, var bottom: Double = 0.0) {
    val width: Double
        get() = right - left

    val height: Double
        get() = bottom - top
}