package com.spacegame.systems

import com.artemis.EntitySubscription
import com.artemis.systems.IteratingSystem
import com.spacegame.util.*

// TODO dont recheck each tick lol
abstract class GenericConnectionSystem : IteratingSystem() {
    private lateinit var connectionManager: ConnectionManager

    abstract var managedConnectionSubscription: EntitySubscription

    override fun process(e: Int) {
        updateConnections(e)
    }

    private fun updateConnections(self: Int) {
        for (other in managedConnectionSubscription.entities) {
            val value = shouldConnect(self, other) ?: continue
            connectionManager.set(self, other, value)
        }
    }

    /**
     * Determines whether the two entities should be connected.
     *
     * @param self The first entity's ID.
     * @param other The second entity's ID.
     * @return true if the entities should be connected, false if they should not be connected, or null if their connection status should not be changed.
     */
    abstract fun shouldConnect(self: Int, other: Int): Boolean?
}