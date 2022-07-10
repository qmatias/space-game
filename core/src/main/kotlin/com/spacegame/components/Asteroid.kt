package com.spacegame.components

import com.artemis.Component
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.spacegame.Assets

class Asteroid : Component() {
    var texture = Assets.ASTEROID
    var size = 200f // TODO use enum for size?
    var rotationSpeed = 20f
}