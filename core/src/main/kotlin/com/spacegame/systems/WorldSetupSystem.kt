package com.spacegame.systems

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.spacegame.components.Asteroid
import com.spacegame.components.Camera
import com.spacegame.components.Position
import net.dermetfan.gdx.assets.AnnotationAssetManager
import kotlin.random.Random

class WorldSetupSystem : BaseSystem() {

    lateinit var camera: Camera

    override fun initialize() {
        println("initializing world")


        for (i in 0..10) {
            val entity: Int = world.create()
            val asteroid = world.edit(entity).create(Asteroid::class.java)
            asteroid.rotationSpeed = Random.nextInt(15, 70).toFloat()
            asteroid.size = Random.nextInt(40, 170).toFloat()

            val position = world.edit(entity).create(Position::class.java)
            val left = camera.camera.position.x - camera.camera.viewportWidth / 2
            val right = camera.camera.position.x + camera.camera.viewportWidth / 2
            val bottom = camera.camera.position.y - camera.camera.viewportHeight / 2
            val top = camera.camera.position.y + camera.camera.viewportHeight / 2
            position.x = Random.nextInt(left.toInt(), right.toInt()).toFloat()
            position.y = Random.nextInt(bottom.toInt(), top.toInt()).toFloat()
        }
    }

    override fun processSystem() { }
}