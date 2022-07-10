package com.spacegame.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.AspectDescriptor
import com.artemis.systems.IntervalIteratingSystem
import com.artemis.systems.IteratingSystem
import com.spacegame.components.Asteroid
import com.spacegame.components.Rotation

@All(Asteroid::class)
class AsteroidRotatorSystem : IntervalIteratingSystem(Aspect.all(), 1 / 20f) {
    lateinit var asteroidMapper: ComponentMapper<Asteroid>
    lateinit var rotationMapper: ComponentMapper<Rotation>
    override fun process(e: Int) {
        val asteroid: Asteroid = asteroidMapper.get(e)
        val rotation: Rotation = rotationMapper.create(e)

        println("rotating ${rotation.rotation} ${asteroid.rotationSpeed} ${world.delta}")

        rotation.rotation += asteroid.rotationSpeed * world.getDelta()
    }
}