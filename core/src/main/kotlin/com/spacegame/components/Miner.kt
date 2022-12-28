package com.spacegame.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.utils.IntBag

class Miner(
    val range: Float = 300f,
) : Component()