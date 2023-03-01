package com.spacegame.systems.connections

import com.artemis.EntitySubscription
import com.artemis.annotations.All
import com.artemis.annotations.One
import com.spacegame.components.EnergyConsumer
import com.spacegame.components.EnergyGenerator
import com.spacegame.components.Powerline

@All(Powerline::class)
class EnergyConnectionSystem : GenericRangeConnectionSystem() {
    @One(Powerline::class, EnergyGenerator::class, EnergyGenerator::class, EnergyConsumer::class)
    override lateinit var managedConnectionSubscription: EntitySubscription
}