package com.spacegame.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.utils.IntBag

class Generator(
    var energyPerSecond: Int = 1
) : Component()
