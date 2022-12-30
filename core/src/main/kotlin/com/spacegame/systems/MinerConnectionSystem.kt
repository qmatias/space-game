package com.spacegame.systems

import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.spacegame.components.*
import com.spacegame.util.*

@All(Miner::class)
class MinerConnectionSystem : GenericRangeConnectionSystem() {
    @All(Asteroid::class)
    override lateinit var managedConnectionSubscription: EntitySubscription
}