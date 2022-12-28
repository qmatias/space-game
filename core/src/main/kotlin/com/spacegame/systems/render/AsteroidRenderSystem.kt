package com.spacegame.systems.render

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.spacegame.Assets
import com.spacegame.components.*
import com.spacegame.util.getNullable
import java.lang.Float.max


@All(Asteroid::class, Size::class, Position::class)
class AsteroidRenderSystem : IteratingSystem() {
    private lateinit var sizeMapper: ComponentMapper<Size>
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var rotationMapper: ComponentMapper<Rotation>

    private val batch = SpriteBatch()

    private lateinit var camera: Camera

    @Wire
    private lateinit var assetManager: Assets

    override fun process(e: Int) {
        val position = positionMapper.get(e)
        val width = sizeMapper.get(e).size
        val texture = TextureRegion(assetManager.get(Assets.ASTEROID))
        val rotation = rotationMapper.getNullable(e)?.rotation ?: 0f
        val ratio = texture.regionWidth / texture.regionHeight.toFloat()
        val height = width * ratio

        batch.draw(
            texture,
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