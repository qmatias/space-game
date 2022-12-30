package com.spacegame.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.utils.IntBag

class Battery(
    var energy: Int = 0,
    var capacity: Int = 10,
) : Component()
