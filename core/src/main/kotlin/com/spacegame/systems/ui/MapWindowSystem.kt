package com.spacegame.systems.ui

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.Assets
import com.spacegame.ui.GameWindow

class MapWindowSystem(
) : BaseSystem() {
    lateinit var window: Table

    @Wire
    lateinit var assetManager: Assets

    override fun initialize() {
        val skin = assetManager.get(Assets.UI_SKIN)

        window = GameWindow("Map", skin)
        window.add(Image(skin, "soldier")).expand().fill()
    }

    override fun processSystem() {}
}
