package com.spacegame.components

import com.artemis.Component
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.spacegame.Assets
import com.spacegame.util.nextFloat
import kotlin.random.Random

class Spinning(
    // radians per second
    var rotationSpeed: Float = 20f
) : Component() {
    companion object {
        fun random() = Spinning(Random.nextFloat(7f, 30f))
    }
}