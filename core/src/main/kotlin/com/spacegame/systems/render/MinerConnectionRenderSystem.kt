package com.spacegame.systems.render

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.spacegame.components.*
import com.spacegame.systems.ConnectionManager
import com.spacegame.util.getNullable
import com.spacegame.util.iterator


@All(Miner::class, Position::class, Connection::class)
class MinerConnectionRenderSystem(
    private val lineWidth: Float = 5f,
    private val color: Color = Color.BLUE,
    private val placingColor: Color = Color.CYAN
) : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var beingPlacedMapper: ComponentMapper<BeingPlaced>
    private lateinit var asteroidMapper: ComponentMapper<Asteroid>

    private lateinit var connectionManager: ConnectionManager

    private val shapeDrawer = buildShapeDrawer()

    private lateinit var camera: Camera

    override fun process(e: Int) {
        val minerPosition = positionMapper.get(e)
        val lineColor = if (beingPlacedMapper.has(e)) placingColor else color

        // draw lines to all asteroid connections
        connectionManager.getConnections(e)
            .filter { asteroidMapper.has(it) }
            .mapNotNull { positionMapper.getNullable(it) }
            .forEach { shapeDrawer.line(minerPosition.x, minerPosition.y, it.x, it.y, lineColor, lineWidth) }
    }


    override fun begin() {
        shapeDrawer.batch.projectionMatrix = camera.camera.combined
        shapeDrawer.update()
        shapeDrawer.batch.begin()
    }

    override fun end() {
        shapeDrawer.batch.end()
    }
}