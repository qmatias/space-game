package com.spacegame.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IntervalIteratingSystem
import com.spacegame.components.*

class BatterySystem : IntervalIteratingSystem(Aspect.all(Battery::class.java, Active::class.java), 1f) {
    private lateinit var batteryMapper: ComponentMapper<Battery>

    var totalEnergyStored = 0
        private set

    var totalEnergyCapacity = 0
        private set

    override fun begin() {
        totalEnergyStored = 0
        totalEnergyCapacity = 0
    }

    override fun process(e: Int) {
        val battery = batteryMapper.get(e)
        totalEnergyStored += battery.energy
        totalEnergyCapacity += battery.capacity
    }
}