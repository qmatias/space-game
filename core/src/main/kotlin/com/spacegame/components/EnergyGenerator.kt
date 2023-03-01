package com.spacegame.components

import com.artemis.Component

class EnergyGenerator(
    var energyPerSecond: Int = 1,
    var energyProduced: Int = 0
) : Component()
