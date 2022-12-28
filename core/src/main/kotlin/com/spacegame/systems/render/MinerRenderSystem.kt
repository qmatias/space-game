package com.spacegame.systems.render

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.spacegame.Assets
import com.spacegame.components.*
import com.spacegame.util.getNullable


@All(Miner::class, Size::class, Position::class)
class MinerRenderSystem(
    // determines how close to the asteroid core the miner is rendered
    // 0 = center, 1 = outer rim
    var heightRatio: Float = 0.75f
) : IteratingSystem() {
    private lateinit var asteroidMapper: ComponentMapper<Asteroid>
    private lateinit var sizeMapper: ComponentMapper<Size>
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var rotationMapper: ComponentMapper<Rotation>

    private val batch = SpriteBatch()

    private lateinit var camera: Camera

    @Wire
    private lateinit var assetManager: Assets

    override fun process(e: Int) {
        val texture = TextureRegion(assetManager.get(Assets.MINER))
        val ratio = texture.regionWidth / texture.regionHeight.toFloat()
        val width = sizeMapper.get(e).size
        val height = width * ratio
        val rotation = rotationMapper.getNullable(e)?.rotation ?: 0f
        val position = positionMapper.get(e).let { Vector2(it.x, it.y) }

        if (asteroidMapper.has(e)) {
            val size = (width + height) / 2
            position.add(Vector2(0f, size * heightRatio).rotateDeg(rotation))
        }

        batch.draw(
            texture,
            position.x - width / 2, position.y - height / 2,
            width / 2, height / 2,
            width, height,
            1f, 1f,
            rotation + 180, // TODO rotate sprite source
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