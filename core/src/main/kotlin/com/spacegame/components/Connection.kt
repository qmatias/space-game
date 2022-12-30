package com.spacegame.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.utils.IntBag

class Connection(
    @EntityId val entities: IntBag = IntBag()
) : Component()