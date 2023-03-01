package com.spacegame.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IntervalIteratingSystem
import com.spacegame.components.*
import com.spacegame.systems.connections.EnergyConnectionSystem

class GeneratorSystem : IntervalIteratingSystem(Aspect.all(EnergyGenerator::class.java, Active::class.java), 1f) {
    private lateinit var activeMapper: ComponentMapper<Active>
    private lateinit var batteryMapper: ComponentMapper<Battery>
    private lateinit var energyGeneratorMapper: ComponentMapper<EnergyGenerator>

    private lateinit var energyConnectionSystem: EnergyConnectionSystem

    var maxEnergyProduction = 0
        private set

    var actualEnergyProduction = 0
        private set

    override fun begin() {
        maxEnergyProduction = 0
        actualEnergyProduction = 0
    }

    override fun process(e: Int) {
        var energy = energyGeneratorMapper.get(e).energyPerSecond
        maxEnergyProduction += energy

        val batteries = getConnectedBatteries(e).toMutableList()

        // fill up connected batteries
        while (energy > 0 && batteries.isNotEmpty()) {
            val battery = batteries.removeFirst()
            val batteryComponent = batteryMapper.get(battery)
            val space = batteryComponent.capacity - batteryComponent.energy
            val used = minOf(space, energy)
            batteryComponent.energy += used
            actualEnergyProduction += used
            energy -= used
        }
    }

    private fun getConnectedBatteries(e: Int): List<Int> {
        val batteries = energyConnectionSystem.getRecursiveConnections(e)
            .filter { activeMapper.has(it) }
            .filter { batteryMapper.has(it) }
            .sortedBy { batteryMapper.get(it).capacity } // fill the lowest capacity batteries first
            .toMutableList()

        // fill ourselves first
        if (batteryMapper.has(e))
            batteries.add(0, e)

        return batteries
    }
}