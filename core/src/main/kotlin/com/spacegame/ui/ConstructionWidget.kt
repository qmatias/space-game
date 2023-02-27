package com.spacegame.ui

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import kotlin.math.round

class ConstructionWidget(skin: Skin) : Table(skin) {
    private var label = UpdatingLabel("Construction: {}%", skin, 0)
    private var bar = ProgressBar(0f, 1f, 0.01f, false, skin)

    init {
        isVisible = false
        top().defaults().left().expandX().fill()
        add(label).row()
        add(bar).row()
    }

    fun update(progress: Float = -1f) {
        label.update(round(progress * 100))
        bar.value = progress
        // hide when done/invalid
        isVisible = 0 <= progress && progress < 1
    }
}