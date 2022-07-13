package com.spacegame.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.spacegame.Assets
import com.spacegame.components.Asteroid
import com.spacegame.components.Camera
import com.spacegame.components.Position
import com.spacegame.components.Rotation
import net.dermetfan.gdx.assets.AnnotationAssetManager

@All(Asteroid::class)
class AsteroidRenderSystem : IteratingSystem() {
    lateinit var asteroidMapper: ComponentMapper<Asteroid>
    lateinit var positionMapper: ComponentMapper<Position>
    lateinit var rotationMapper: ComponentMapper<Rotation>

    private val batch = SpriteBatch()

    lateinit var camera: Camera

    @Wire
    lateinit var assetManager: Assets

    override fun process(e: Int) {
        val asteroid: Asteroid = asteroidMapper.get(e)
        val position: Position = positionMapper.create(e)
        val rotation: Rotation = rotationMapper.create(e)

        val texture = TextureRegion(assetManager.get(asteroid.texture))
        val ratio = texture.regionWidth / texture.regionHeight
        val width = asteroid.size
        val height = asteroid.size * ratio
        batch.draw(
            texture,
            position.x - width / 2, position.y - height / 2,
            width / 2, height / 2,
            width, height,
            1f, 1f,
            rotation.rotation
        )
    }

    override fun begin() {
        super.begin()
        batch.projectionMatrix = camera.camera.combined
        batch.begin()
    }

    override fun end() {
        super.end()
        batch.end()
    }
}