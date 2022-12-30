package com.spacegame.components

import com.artemis.Component
import com.badlogic.gdx.math.Interpolation
import com.spacegame.util.nextFloat
import net.mostlyoriginal.api.component.common.Tweenable
import kotlin.random.Random

class Rotation(
    // degrees
    // between -180 and 180
    // 0 is facing up
    var rotation: Float = 0f
) : Component(), Tweenable<Rotation> {
    override fun tween(a: Rotation, b: Rotation, value: Float) {
        rotation = Interpolation.linear.apply(a.rotation, b.rotation, value)
    }

    fun normalize() {
        while (rotation < 180)
            rotation += 360f
        while (180 < rotation)
            rotation -= 360f
    }

    companion object {
        fun random() = Rotation(Random.nextFloat(0f, 360f))
    }
}