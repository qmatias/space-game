package com.spacegame.systems

import com.artemis.BaseSystem
import com.spacegame.Assets
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

            world.edit(entity)
                .add(Selectable())
                .add(Collideable())
                .add(Rotation.random())
                .add(Spinning.random())
                .add(Asteroid())
                .add(Active())
                .add(Texture(Assets.ASTEROID))

            val position = world.edit(entity).create(Position::class.java)
            val size = world.edit(entity).create(Size::class.java)

            var bb: BoundingBox
            do {
                position.x = Random.nextFloat(screenLeft, screenRight)
                position.y = Random.nextFloat(screenBottom, screenTop)

                bb = CircularBoundingBox(position.x, position.y, size.size / 2)
            } while (others.any { bb.collides(it) })

            others.add(bb)
        }
    }

    override fun processSystem() {}
}