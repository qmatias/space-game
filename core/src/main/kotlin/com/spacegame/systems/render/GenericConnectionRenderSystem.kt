package com.spacegame.systems.render

import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.spacegame.components.*
import com.spacegame.systems.connections.GenericConnectionSystem
import com.spacegame.util.getNullable
import space.earlygrey.shapedrawer.ShapeDrawer


abstract class GenericConnectionRenderSystem<T : GenericConnectionSystem> : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>

    abstract var connectionSystem: T

    @Wire
    private lateinit var shapeDrawer: ShapeDrawer

    private lateinit var camera: Camera

    class ConnectionSet {
        val map = HashSet<Pair<Int, Int>>()
        fun addConnection(a: Int, b: Int) = map.add(Pair(minOf(a, b), maxOf(a, b)))
        fun reset() = map.clear()
    }

    private val drawnConnections = ConnectionSet()

    override fun begin() {
        drawnConnections.reset()

        shapeDrawer.batch.projectionMatrix = camera.camera.combined
        shapeDrawer.update()
        shapeDrawer.batch.begin()
    }

    override fun end() {
        shapeDrawer.batch.end()
    }

    abstract fun drawConnection(from: Int, to: Int)

    fun drawLine(from: Int, to: Int, color: Color, width: Float = 1f) {
        val a = positionMapper.getNullable(from) ?: return
        val b = positionMapper.getNullable(to) ?: return
        shapeDrawer.line(a.x, a.y, b.x, b.y, color, width)
    }

    override fun process(e: Int) {
        connectionSystem.getShallowConnections(e)
            .filter { positionMapper.has(it) }
            .filter { drawnConnections.addConnection(e, it) }
            .forEach {
                drawConnection(e, it)
            }
    }
}