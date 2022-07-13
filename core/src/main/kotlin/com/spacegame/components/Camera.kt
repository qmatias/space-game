package com.spacegame.components

import com.artemis.Component
import com.badlogic.gdx.graphics.OrthographicCamera
import net.mostlyoriginal.api.Singleton

@Singleton
class Camera : Component() {
    var camera = OrthographicCamera(1600f, 900f)
}