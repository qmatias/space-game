package com.spacegame.systems.connections

import com.artemis.WorldConfigurationBuilder


fun WorldConfigurationBuilder.withConnections(): WorldConfigurationBuilder {
    with(EnergyConnectionSystem())
    with(AsteroidMinerConnectionSystem())
    return this
}

class ConnectionParityException : IllegalStateException()