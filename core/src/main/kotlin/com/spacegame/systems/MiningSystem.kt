package com.spacegame.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IntervalIteratingSystem
import com.spacegame.components.Asteroid
import com.spacegame.components.BeingPlaced
import com.spacegame.components.Miner
import com.spacegame.components.Resources
import com.spacegame.util.getNullable
import com.spacegame.util.iterator

class MiningSystem : IntervalIteratingSystem(Aspect.all(Miner::class.java).exclude(BeingPlaced::class.java), 1f) {
    private lateinit var asteroidMapper: ComponentMapper<Asteroid>

    private lateinit var connectionManager: ConnectionManager

    private lateinit var resources: Resources

    var mineralsPerSecond = 0
        private set


    override fun process(e: Int) {
        // total sum of all connected asteroid mineralsPerSeconds's
        val totalMinerals = connectionManager.getConnections(e)
            .sumOf { asteroidMapper.getNullable(it)?.mineralsPerSecond ?: 0 }

        resources.minerals += totalMinerals
    }
}