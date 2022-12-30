package com.spacegame.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IntervalIteratingSystem
import com.spacegame.components.*
import com.spacegame.util.getNullable

class MiningSystem : IntervalIteratingSystem(Aspect.all(Miner::class.java, Active::class.java), 1f) {
    private lateinit var asteroidMapper: ComponentMapper<Asteroid>
    private lateinit var minerMapper: ComponentMapper<Miner>
    private lateinit var activeMapper: ComponentMapper<Active>

    private lateinit var connectionManager: ConnectionManager

    private lateinit var resources: Resources

    var mineralsPerSecond = 0
        private set

    override fun begin() {
        mineralsPerSecond = 0
    }

    override fun end() {
        resources.minerals += mineralsPerSecond
    }

    override fun process(e: Int) {
        // total sum of all connected asteroid mineralsPerSeconds's
        val totalMinerals = connectionManager.get(e)
            .filter { activeMapper.has(it) }
            .sumOf { asteroidMapper.getNullable(it)?.mineralsPerSecond ?: 0 }

        val miner = minerMapper.get(e)
        miner.mineralsMined += totalMinerals

        mineralsPerSecond += totalMinerals
    }
}