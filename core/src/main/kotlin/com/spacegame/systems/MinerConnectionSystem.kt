package com.spacegame.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.artemis.utils.IntBag
import com.spacegame.components.*
import com.spacegame.util.*

// TODO dont recheck each tick lol
@All(Miner::class)
class MinerConnectionSystem : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var minerMapper: ComponentMapper<Miner>
    private lateinit var asteroidMapper: ComponentMapper<Asteroid>

    private lateinit var connectionManager: ConnectionManager
    private lateinit var collisionSystem: CollisionSystem

    @All(Asteroid::class)
    private lateinit var asteroidSubscription: EntitySubscription

    override fun process(e: Int) {
        updateConnections(e)
    }

    fun updateConnections(miner: Int) {
        val minerBB = getMinerRangeBB(miner)

        // remove all asteroid connections
        connectionManager.getConnections(miner)
            .filter { asteroidMapper.has(it) }
            .forEach { connectionManager.removeConnection(miner, it) }

        for (asteroid in asteroidSubscription.entities) {
            val asteroidBB = collisionSystem.getEntityBB(asteroid)

            // add them back if we're still colliding
            if (minerBB.collides(asteroidBB)) {
                connectionManager.addConnection(miner, asteroid)
            }
        }
    }

    fun getMinerRangeBB(e: Int): BoundingBox {
        val miner = minerMapper.get(e)
        val position = positionMapper.getNullable(e) ?: return NullBoundingBox()
        return CircularBoundingBox(position.x, position.y, miner.range)
    }
}