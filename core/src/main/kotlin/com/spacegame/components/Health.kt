package com.spacegame.components

import com.artemis.Component

class Health(
    var health: Int = 100,
    var maxHealth: Int = 100
) : Component()