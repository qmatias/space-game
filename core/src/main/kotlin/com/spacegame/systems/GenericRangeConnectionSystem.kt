package com.spacegame.systems

import com.artemis.ComponentMapper
import com.spacegame.components.*

abstract class GenericRangeConnectionSystem : GenericConnectionSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var rangeMapper: ComponentMapper<Range>

    private lateinit var collisionSystem: CollisionSystem

    override fun shouldConnect(self: Int, other: Int): Boolean? {
        val rangeBB = collisionSystem.getRangeBB(self)
        val otherBB = collisionSystem.getEntityBB(other)
        return rangeBB.collides(otherBB)
    }
}