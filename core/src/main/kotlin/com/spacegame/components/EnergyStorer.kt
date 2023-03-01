package com.spacegame.components

import com.artemis.Component

class EnergyStorer(
    var energy: Int = 0,
    var capacity: Int = 10,
) : Component()
