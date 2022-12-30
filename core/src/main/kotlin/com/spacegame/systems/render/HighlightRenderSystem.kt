package com.spacegame.systems.render

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.One
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.spacegame.components.*
import com.spacegame.util.getNullable
import space.earlygrey.shapedrawer.ShapeDrawer


@All(Size::class, Position::class)
@One(Selected::class, Highlight::class)
class HighlightRenderSystem(
    private val lineWidth: Float = 5f
) : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var sizeMapper: ComponentMapper<Size>
    private lateinit var highlightMapper: ComponentMapper<Highlight>

    @Wire
    private lateinit var shapeDrawer: ShapeDrawer

    private lateinit var camera: Camera

    override fun process(e: Int) {
        val position = positionMapper.get(e)
        val size = sizeMapper.get(e).size
        val color = highlightMapper.getNullable(e)?.color ?: Color.GREEN
        shapeDrawer.rectangle(position.x - size / 2, position.y - size / 2, size, size, color, lineWidth)
    }

    override fun begin() {
        shapeDrawer.batch.projectionMatrix = camera.camera.combined
        shapeDrawer.batch.begin()
    }

    override fun end() {
        shapeDrawer.batch.end()
    }
}