package com.spacegame.ui

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.components.Health
import kotlin.math.round

class HealthWidget(skin: Skin) : Table(skin) {
    private var label = UpdatingLabel("Health: {}", skin, 0)
    private var bar = ProgressBar(0f, 1f, 0.01f, false, skin)

    init {
        isVisible = false
        top().defaults().left().expandX().fill()
        add(label).row()
        add(bar).row()
    }

    fun update(health: Health?) {
        if (health == null) {
            isVisible = false
            return
        }

        isVisible = true
        label.update(health.health)
        bar.value = health.health / health.maxHealth.toFloat()
    }
}