package com.spacegame.systems.render

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.One
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.spacegame.components.*


@All(Miner::class, Size::class, Position::class)
@One(ShowRange::class, Selected::class)
class RangeRenderSystem(
    private val lineWidth: Float = 2f,
    private val color: Color = Color.WHITE
) : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var minerMapper: ComponentMapper<Miner>

    private val shapeRenderer = ShapeRenderer()

    private lateinit var camera: Camera

    override fun process(e: Int) {
        val position = positionMapper.get(e)
        val range = minerMapper.get(e).range
        shapeRenderer.circle(position.x, position.y, range)
    }

    override fun begin() {
        shapeRenderer.color = color
        shapeRenderer.projectionMatrix = camera.camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
    }

    override fun end() {
        Gdx.gl.glLineWidth(lineWidth)
        shapeRenderer.end()
    }
}