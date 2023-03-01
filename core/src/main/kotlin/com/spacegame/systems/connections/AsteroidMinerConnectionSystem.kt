package com.spacegame.systems.connections

import com.artemis.EntitySubscription
import com.artemis.annotations.All
import com.artemis.annotations.One
import com.spacegame.components.*

@All(AsteroidMiner::class)
class AsteroidMinerConnectionSystem : GenericRangeConnectionSystem() {
    @One(Asteroid::class)
    override lateinit var managedConnectionSubscription: EntitySubscription
}