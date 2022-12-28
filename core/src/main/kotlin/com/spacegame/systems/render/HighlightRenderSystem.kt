package com.spacegame.systems.render

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.One
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.spacegame.components.*


@All(Size::class, Position::class)
@One(Selected::class, Highlight::class)
class HighlightRenderSystem(
    private val lineWidth: Float = 5f,
    private val color: Color = Color.GREEN
) : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var sizeMapper: ComponentMapper<Size>

    private val shapeRenderer = ShapeRenderer()

    private lateinit var camera: Camera

    override fun process(e: Int) {
        val position = positionMapper.get(e)
        val size = sizeMapper.get(e).size
        shapeRenderer.rect(position.x - size / 2, position.y - size / 2, size, size)
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