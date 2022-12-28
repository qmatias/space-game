package com.spacegame.systems

import com.artemis.BaseSystem
import com.spacegame.components.*
import com.spacegame.util.*
import kotlin.random.Random

class TestingWorldSetupSystem() : BaseSystem() {
    private lateinit var camera: Camera

    override fun initialize() {
        val others = mutableListOf<BoundingBox>()

        val screenLeft = camera.camera.position.x - camera.camera.viewportWidth / 2
        val screenRight = camera.camera.position.x + camera.camera.viewportWidth / 2
        val screenBottom = camera.camera.position.y - camera.camera.viewportHeight / 2
        val screenTop = camera.camera.position.y + camera.camera.viewportHeight / 2

        for (i in 0..10) {
            val entity: Int = world.create()

            world.edit(entity).create(Selectable::class.java)
            world.edit(entity).create(Collideable::class.java)

            val rotation = world.edit(entity).create(Rotation::class.java)
            rotation.rotation = Random.nextFloat(0f, 360f)

            val spinning = world.edit(entity).create(Spinning::class.java)
            spinning.rotationSpeed = Random.nextFloat(7f, 30f)

            val position = world.edit(entity).create(Position::class.java)
            val size = world.edit(entity).create(Size::class.java)

            var bb: BoundingBox
            do {
                position.x = Random.nextFloat(screenLeft, screenRight)
                position.y = Random.nextFloat(screenBottom, screenTop)

                bb = getCircularBB(position, size)
            } while (others.any { bb.collides(it) })

            others.add(bb)

            world.edit(entity).create(Asteroid::class.java)
        }
    }

    override fun processSystem() {}
}