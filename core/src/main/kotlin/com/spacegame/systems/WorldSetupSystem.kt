package com.spacegame.systems

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.spacegame.components.Asteroid
import com.spacegame.components.Position
import net.dermetfan.gdx.assets.AnnotationAssetManager
import kotlin.random.Random

class WorldSetupSystem : BaseSystem() {
    override fun initialize() {
        println("initializing world")


        for (i in 0..10) {
            val entity: Int = world.create()
            val asteroid = world.edit(entity).create(Asteroid::class.java)
            asteroid.rotationSpeed = Random.nextInt(15, 70).toFloat()
            asteroid.size = Random.nextInt(40, 170).toFloat()

            val position = world.edit(entity).create(Position::class.java)
            position.x = Random.nextInt(0, Gdx.graphics.width).toFloat()
            position.y = Random.nextInt(0, Gdx.graphics.height).toFloat()
        }
    }

    override fun processSystem() { }
}