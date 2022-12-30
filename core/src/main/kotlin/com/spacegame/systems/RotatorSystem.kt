package com.spacegame.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Exclude
import com.artemis.systems.IteratingSystem
import com.spacegame.components.Asteroid
import com.spacegame.components.Rotation
import com.spacegame.components.Selected
import com.spacegame.components.Spinning

@All(Spinning::class)
@Exclude(Selected::class)
class RotatorSystem : IteratingSystem() {
    private lateinit var spinningMapper: ComponentMapper<Spinning>
    private lateinit var rotationMapper: ComponentMapper<Rotation>
    override fun process(e: Int) {
        val spinning = spinningMapper.get(e)
        val rotation = rotationMapper.create(e)

        rotation.rotation += spinning.rotationSpeed * world.getDelta()
        rotation.normalize()
    }
}