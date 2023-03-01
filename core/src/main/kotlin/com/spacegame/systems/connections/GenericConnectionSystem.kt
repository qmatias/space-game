package com.spacegame.systems.connections

import com.artemis.EntitySubscription
import com.artemis.systems.IteratingSystem
import com.spacegame.util.*

// TODO dont recheck each tick somehow?
abstract class GenericConnectionSystem : IteratingSystem() {
    abstract var managedConnectionSubscription: EntitySubscription

    private val connections = mutableMapOf<Int, MutableSet<Int>>()

    override fun inserted(e: Int) {
        connections[e] = mutableSetOf()
    }

    override fun process(e: Int) {
        updateConnectionsFor(e)
    }

    override fun removed(e: Int) {
        connections.remove(e)
    }

    private fun updateConnectionsFor(self: Int) {
        for (other in managedConnectionSubscription.entities) {
            if (self == other) continue
            val isConnected = hasShallowConnection(self, other)
            val value = shouldConnect(self, other, isConnected) ?: continue
            setConnection(self, other, value)
        }
    }

    fun addConnection(from: Int, to: Int) = connections[from]?.add(to) ?: false

    fun removeConnection(from: Int, to: Int) = connections[from]?.remove(to) ?: false

    fun hasShallowConnection(from: Int, to: Int): Boolean = to in getShallowConnections(from)

    fun hasRecursiveConnection(from: Int, to: Int): Boolean = to in getRecursiveConnections(from)

    fun getShallowConnections(e: Int): Set<Int> = connections[e] ?: emptySet()

    fun getRecursiveConnections(e: Int): Set<Int> {
        val connections = mutableSetOf<Int>()
        val queue = mutableListOf(e)
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            connections.add(next)
            queue.addAll(getShallowConnections(next).filter { it !in connections })
        }
        connections.remove(e)
        return connections
    }

    fun setConnection(from: Int, to: Int, value: Boolean): Boolean = when (value) {
        true -> addConnection(from, to)
        false -> removeConnection(from, to)
    }

    /**
     * Determines whether the two entities should be connected.
     *
     * @param self The first entity's ID.
     * @param other The second entity's ID.
     * @param current The current connection status between the two entities.
     * @return true if the entities should be connected, false if they should not be connected, or null if their connection status should not be changed.
     */
    abstract fun shouldConnect(self: Int, other: Int, current: Boolean): Boolean?
}