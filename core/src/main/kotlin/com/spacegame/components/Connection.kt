package com.spacegame.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.utils.IntBag

class Connection(
    @EntityId var entities: IntBag = IntBag()
) : Component()