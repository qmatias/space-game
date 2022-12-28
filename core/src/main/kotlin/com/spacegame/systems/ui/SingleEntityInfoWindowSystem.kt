package com.spacegame.systems.ui

import com.artemis.BaseEntitySystem
import com.badlogic.gdx.scenes.scene2d.ui.Table

abstract class SingleEntityInfoWindowSystem : BaseEntitySystem() {
    val active get() = entityIds.size() == 1

    lateinit var window: Table

    abstract fun clearWindow()
    abstract fun updateWindowFor(e: Int)

    override fun processSystem() {
        if (entityIds.size() == 1) {
            updateWindowFor(entityIds[0])
        } else {
            clearWindow()
        }
    }
}