package com.spacegame.components

import com.artemis.Component
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.spacegame.Assets

class Spinning(
    // radians per second
    var rotationSpeed: Float = 20f
) : Component()