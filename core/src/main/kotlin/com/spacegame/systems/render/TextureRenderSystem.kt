package com.spacegame.systems.render

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.spacegame.Assets
import com.spacegame.components.*
import com.spacegame.util.getNullable


@All(Texture::class, Size::class, Position::class)
class TextureRenderSystem : IteratingSystem() {
    private lateinit var sizeMapper: ComponentMapper<Size>
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var rotationMapper: ComponentMapper<Rotation>
    private lateinit var textureMapper: ComponentMapper<Texture>

    private lateinit var camera: Camera

    @Wire
    private lateinit var assetManager: Assets

    @Wire(name = "batch")
    private lateinit var batch: Batch

    override fun process(e: Int) {
        val position = positionMapper.get(e)
        val width = sizeMapper.get(e).size
        val texture = textureMapper.get(e)
        val textureRegion = TextureRegion(assetManager.get(texture.descriptor))
        val rotation = rotationMapper.getNullable(e)?.rotation ?: 0f
        val ratio = textureRegion.regionHeight / textureRegion.regionWidth.toFloat()
        val height = width * ratio

        batch.draw(
            textureRegion,
            position.x - width / 2, position.y - height / 2,
            width / 2, height / 2,
            width, height,
            1f, 1f,
            rotation
        )
    }

    override fun begin() {
        batch.projectionMatrix = camera.camera.combined
        batch.begin()
    }

    override fun end() {
        batch.end()
    }
}