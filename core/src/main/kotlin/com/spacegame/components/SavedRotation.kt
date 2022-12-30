package com.spacegame.components

import com.artemis.Component
import com.badlogic.gdx.math.Interpolation
import net.mostlyoriginal.api.component.common.Tweenable

class SavedRotation(
    var rotation: Float = 0f // radians
) : Component()