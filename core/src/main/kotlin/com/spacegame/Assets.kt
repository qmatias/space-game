package com.spacegame

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture

class Assets : AssetManager() {
    fun loadAll() {
        load(ASTEROID)
        finishLoading()
    }

    companion object {
        val ASTEROID = texture("spaceshooter/PNG/Meteors/meteorBrown_big1.png")
    }
}

private fun texture(path: String) = AssetDescriptor(path, Texture::class.java)