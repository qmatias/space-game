package com.spacegame.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.utils.IntBag

class Miner(
    var mineralsMined: Int = 0
) : Component()