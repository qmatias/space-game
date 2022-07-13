package com.spacegame

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.spacegame.systems.AsteroidRenderSystem
import com.spacegame.systems.AsteroidRotatorSystem
import com.spacegame.systems.InputSystem
import com.spacegame.systems.WorldSetupSystem
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use
import net.dermetfan.gdx.assets.AnnotationAssetManager
import net.mostlyoriginal.api.SingletonPlugin
import java.util.InputMismatchException
import kotlin.math.min

const val MIN_DELTA = 1 / 15f

class FirstScreen(assetManager: Assets) : KtxScreen {
    private val inputSystem = InputSystem()

    private val world = World(
        WorldConfigurationBuilder()
            .with(SingletonPlugin())
            .with(WorldSetupSystem())
            .with(InputSystem())
            .with(AsteroidRenderSystem())
            .with(AsteroidRotatorSystem())
            .build()
            .register(assetManager)
    )

    override fun hide() {
        super.hide()
    }

    override fun show() {
        super.show()
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        world.setDelta(min(delta, MIN_DELTA));
        world.process()
    }
}