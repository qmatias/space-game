package com.spacegame.components

import com.artemis.Component
import com.badlogic.gdx.graphics.OrthographicCamera
import net.mostlyoriginal.api.Singleton

@Singleton
class Resources(
    var minerals: Int = 150,
    var ships: Int = 0
) : Component()