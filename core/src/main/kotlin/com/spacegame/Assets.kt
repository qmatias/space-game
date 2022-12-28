package com.spacegame

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
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
        val MINER = add<Texture>("kenny_spaceshooterextension/PNG/Sprites/Station/spaceStation_024.png")
        val SOLAR_STATION = add<Texture>("kenny_spaceshooterextension/PNG/Sprites/Station/spaceStation_022.png")
        val SHIP_1 = add<Texture>("kenny_spaceshooterextension/PNG/Sprites/Rockets/spaceRockets_001.png")
        val SHIP_2 = add<Texture>("kenny_spaceshooterextension/PNG/Sprites/Rockets/spaceRockets_002.png")
        val SHIP_3 = add<Texture>("kenny_spaceshooterextension/PNG/Sprites/Rockets/spaceRockets_003.png")
        val UI_SKIN = add<Skin>("star-soldier/star-soldier.json")
    }
}
