package com.spacegame.components

import com.artemis.Component
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.spacegame.Assets

class Texture(
    var descriptor: AssetDescriptor<Texture> = Assets.LOGO
) : Component()