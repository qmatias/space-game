package com.spacegame.systems

import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.annotations.All
import com.artemis.annotations.One
import com.artemis.systems.IteratingSystem
import com.spacegame.components.*
import com.spacegame.util.*

@All(SolarStation::class)
class SolarStationConnectionSystem : GenericRangeConnectionSystem() {
    @One(Miner::class, SolarStation::class)
    override lateinit var managedConnectionSubscription: EntitySubscription
}