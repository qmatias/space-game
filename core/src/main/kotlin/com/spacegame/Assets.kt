package com.spacegame

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class Assets : AssetManager() {
    fun loadAll() {
        allDescriptors.forEach { load(it) }
        finishLoading()
    }

    companion object {
        val allDescriptors = mutableListOf<AssetDescriptor<*>>()
        private inline fun <reified T> add(path: String): AssetDescriptor<T> =
            AssetDescriptor(path, T::class.java).also { allDescriptors.add(it) }

        val LOGO = add<Texture>("logo.png")
        val ASTEROID = add<Texture>("spaceshooter/PNG/Meteors/meteorBrown_big1.png")
        val MINERAL_MINER = add<Texture>("midjourney/mineral-miner.png")
        val ENERGY_RELAY = add<Texture>("midjourney/energy-relay.png")
        val SOLAR_STATION = add<Texture>("midjourney/solar-station.png")
        val BATTERY = add<Texture>("midjourney/battery.png")
        val REPAIR_STATION = add<Texture>("midjourney/repair-station.png")
        val MISSILE_LAUNCHER = add<Texture>("midjourney/missile-launcher.png")
        val LASER_CANNON = add<Texture>("midjourney/laser-cannon.png")
        val UI_SKIN = add<Skin>("star-soldier/star-soldier.json")
    }
}
