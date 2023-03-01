package com.spacegame.ui

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.components.EnergyStorer

class BatteryWidget(skin: Skin) : Table(skin) {
    private var label = UpdatingLabel("Energy: {} / {}", skin, 0)
    private var bar = ProgressBar(0f, 1f, 0.01f, false, skin)

    init {
        isVisible = false
        top().defaults().left().expandX().fill()
        add(label).row()
        add(bar).row()
    }

    fun update(energy: Int, capacity: Int) {
        isVisible = true
        label.update(energy, capacity)
        bar.value = energy / capacity.toFloat()
    }

    fun update(energyStorer: EnergyStorer? = null) {
        if (energyStorer == null) {
            isVisible = false
            return
        }

        update(energyStorer.energy, energyStorer.capacity)
    }
}
