package com.spacegame.systems

import com.artemis.Aspect
import com.artemis.BaseSystem
import com.badlogic.gdx.math.Vector2
import com.spacegame.Assets
import com.spacegame.components.*
import com.spacegame.events.KeyTypedEvent
import com.spacegame.util.iterator
import com.spacegame.util.vec2FromAngle
import com.spacegame.util.voroni.Delaunay
import com.spacegame.util.voroni.Rectangle
import com.spacegame.util.voroni.Voronoi
import net.mostlyoriginal.api.event.common.Subscribe
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

class ProcGenWorldSetupSystem(
    private val asteroidCount: Int = 800,
    private val mapSize: Double = 80000.0,
) : BaseSystem() {
    val others = mutableListOf<BoundingBox>()

    fun addAsteroid(x: Float, y: Float, size: Float): Boolean {

        val bb = CircularBoundingBox(x, y, size * 0.6f)
        if (others.any { it.collides(bb) }) {
            return false
        }
        others.add(bb)


        world.edit(world.create())
            .add(Selectable())
            .add(Collideable())
            .add(Rotation.random())
            .add(Spinning.random())
            .add(Asteroid())
            .add(Active())
            .add(Texture(Assets.ASTEROID))
            .add(Position(x, y))
            .add(Size(size))

        return true
    }

    override fun initialize() {
        regen()
    }

    @Subscribe(ignoreCancelledEvents = true, priority = 5000)
    fun onKeyTyped(e: KeyTypedEvent) {
        if (e.character == 'm') {
            regen()
            e.isCancelled = true
        }
    }

    fun regen() {
        // delete all asteroids
        others.clear()
        world.aspectSubscriptionManager
            .get(Aspect.all(Asteroid::class.java))
            .entities.iterator()
            .forEach { world.delete(it) }

        val screen = Rectangle(-mapSize / 2, mapSize / 2, mapSize / 2, -mapSize / 2)

        val coords = mutableListOf<Double>()
        for (i in 0 until asteroidCount) {
            val pos = asteroidPos()
            coords.add(pos.x.toDouble())
            coords.add(pos.y.toDouble())
        }

        val voronoi = Voronoi(Delaunay(coords), screen)
        voronoi.getCellsCenterCoordinates()
            .forEach { (x, y) ->
                val size = asteroidSize(x, y)
                addAsteroid(x.toFloat(), y.toFloat(), size)
            }
    }

    private fun asteroidSize(x: Double, y: Double): Float {
        return max(40 * Random.nextFloat().pow(2), 5f)
    }

    private fun asteroidPos(): Vector2 {
        val theta = Random.nextDouble() * 2 * Math.PI
        val r = 3 * Random.nextDouble().pow(1.3) * mapSize / 2.0
        return vec2FromAngle(theta, r)
    }

    override fun processSystem() {
    }
}