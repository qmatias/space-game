package com.spacegame

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture

enum class Structure(val icon: AssetDescriptor<Texture>, val price: Int) {
    MINER(Assets.MINER, 50),
    SOLAR_STATION(Assets.SOLAR_STATION, 50),
    SHIP_1(Assets.SHIP_1, 2000),
    SHIP_2(Assets.SHIP_2, 2000),
    SHIP_3(Assets.SHIP_3, 2000)
}