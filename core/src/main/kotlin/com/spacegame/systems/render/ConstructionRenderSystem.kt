package com.spacegame.systems.render

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.Viewport
import com.spacegame.Assets
import com.spacegame.components.*
import space.earlygrey.shapedrawer.ShapeDrawer


@All(Size::class, Position::class, Construction::class)
class ConstructionRenderSystem : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var sizeMapper: ComponentMapper<Size>
    private lateinit var constructionMapper: ComponentMapper<Construction>

    private lateinit var camera: Camera

    @Wire
    private lateinit var assetManager: Assets

    @Wire(name = "batch")
    private lateinit var batch: Batch

    private lateinit var skin: Skin

    private val lineWidth: Float = 4f
    private val spacing: Float = 15f

    private val bars = mutableMapOf<Int, ProgressBar>()

    override fun initialize() {
        skin = assetManager.get(Assets.UI_SKIN)
    }

    override fun inserted(e: Int) {
        bars[e] = ProgressBar(0f, 1f, 0.01f, false, skin)
    }

    override fun removed(e: Int) {
        bars.remove(e)
    }

    override fun process(e: Int) {
        val position = positionMapper.get(e)
        val construction = constructionMapper.get(e)
        val size = sizeMapper.get(e).size

        bars[e]!!.apply {
            value = construction.progress
            x = position.x - size / 2
            y = position.y + size / 2 + spacing
            width = size
            height = lineWidth
        }.draw(batch, 1f)
    }

    override fun begin() {
        batch.projectionMatrix = camera.camera.combined
        batch.begin()
    }

    override fun end() {
        batch.end()
    }
}