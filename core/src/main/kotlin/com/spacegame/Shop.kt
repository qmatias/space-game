package com.spacegame

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture

enum class Structure(val prettyName: String, val icon: AssetDescriptor<Texture>, val price: Int) {
    MINERAL_MINER("Mineral Miner", Assets.MINERAL_MINER, 50),
    ENERGY_RELAY("Energy Relay", Assets.ENERGY_RELAY, 10),
    SOLAR_STATION("Solar Station", Assets.SOLAR_STATION, 50),
    REPAIR_STATION("Repair Station", Assets.REPAIR_STATION, 200),
    MISSILE_LAUNCHER("Missile Launcher", Assets.MISSILE_LAUNCHER, 400),
    LASER_CANNON("Laser Cannon", Assets.LASER_CANNON, 400),
    BATTERY("Battery", Assets.BATTERY, 100),
}