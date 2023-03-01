package com.spacegame.systems.connections

import com.artemis.ComponentMapper
import com.spacegame.components.*
import com.spacegame.systems.CollisionSystem

abstract class GenericRangeConnectionSystem : GenericConnectionSystem() {
    private lateinit var collisionSystem: CollisionSystem

    override fun shouldConnect(self: Int, other: Int, isConnected: Boolean): Boolean? {
        val rangeBB = collisionSystem.getRangeBB(self)
        val otherBB = collisionSystem.getEntityBB(other)
        return rangeBB.collides(otherBB)
    }
}