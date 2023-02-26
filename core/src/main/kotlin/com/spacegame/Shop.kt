package com.spacegame

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture

enum class Structure(val icon: AssetDescriptor<Texture>, val price: Int) {
    MINERAL_MINER(Assets.MINERAL_MINER, 50),
    ENERGY_RELAY(Assets.ENERGY_RELAY, 10),
    SOLAR_STATION(Assets.SOLAR_STATION, 50),
    REPAIR_STATION(Assets.REPAIR_STATION, 200),
    MISSILE_LAUNCHER(Assets.MISSILE_LAUNCHER, 400),
    LASER_CANNON(Assets.LASER_CANNON, 400),
    BATTERY(Assets.BATTERY, 100),
}