package com.spacegame.systems.render

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.spacegame.components.*
import com.spacegame.systems.ConnectionManager
import space.earlygrey.shapedrawer.ShapeDrawer
import java.lang.Integer.max
import java.lang.Integer.min


@All(Position::class, Connection::class)
class ConnectionRenderSystem(
    private val lineWidth: Float = 5f,
) : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var asteroidMapper: ComponentMapper<Asteroid>
    private lateinit var minerMapper: ComponentMapper<Miner>
    private lateinit var solarStationMapper: ComponentMapper<SolarStation>
    private lateinit var selectedMapper: ComponentMapper<Selected>

    private lateinit var connectionManager: ConnectionManager

    @Wire
    private lateinit var shapeDrawer: ShapeDrawer

    private lateinit var camera: Camera

    class ConnectionSet {
        val map = HashSet<Pair<Int, Int>>()
        fun add(a: Int, b: Int) = map.add(Pair(minOf(a, b), maxOf(a, b)))
        fun clear() = map.clear()
    }

    private val connections = ConnectionSet()

    override fun begin() {
        connections.clear()

        shapeDrawer.batch.projectionMatrix = camera.camera.combined
        shapeDrawer.update()
        shapeDrawer.batch.begin()
    }

    override fun end() {
        shapeDrawer.batch.end()
    }

    override fun process(e: Int) {
        connectionManager.get(e)
            .filter { positionMapper.has(it) }
            .filter { connections.add(e, it) }
            .forEach {
                val color = determineConnectionColor(e, it)
                val a = positionMapper.get(e)
                val b = positionMapper.get(it)
                shapeDrawer.line(a.x, a.y, b.x, b.y, color, lineWidth)
            }
    }

    fun determineConnectionColor(a: Int, b: Int): Color {
//        val isSelected = selectedMapper.has(a) || selectedMapper.has(b)
//        if (isSelected)
//            return Color.CORAL
//
        val isSolarConnection = solarStationMapper.has(a) || solarStationMapper.has(b)
        if (isSolarConnection)
            return Color.MAGENTA

        val isMinerConnection = minerMapper.has(a) || minerMapper.has(b)
        if (isMinerConnection)
            return Color.BLUE

        return Color.BLACK
    }


}