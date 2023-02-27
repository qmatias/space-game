package com.spacegame.systems

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IntervalIteratingSystem
import com.artemis.systems.IteratingSystem
import com.spacegame.components.*

@All(Construction::class)
class ConstructionSystem : IteratingSystem() {
    private lateinit var constructionMapper: ComponentMapper<Construction>
    override fun removed(e: Int) {
        world.edit(e)
            .remove(Construction::class.java)
            .add(Active())
    }

    override fun process(e: Int) {
        val construction = constructionMapper.get(e)
        construction.progress += construction.speed * world.delta
        if (construction.progress >= 1f) {
            constructionMapper.remove(e)
        }
    }
}