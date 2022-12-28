package com.spacegame

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.scenes.scene2d.Stage
import com.spacegame.events.*
import com.spacegame.systems.*
import com.spacegame.systems.render.*
import com.spacegame.systems.ui.*
import ktx.app.KtxScreen
import ktx.app.clearScreen
import net.mostlyoriginal.api.SingletonPlugin
import net.mostlyoriginal.api.event.common.EventSystem
import kotlin.math.min

const val MAX_DELTA = 1 / 15f

class GameScreen(assetManager: Assets) : KtxScreen {
    private val world = World(
        WorldConfigurationBuilder()
            .with(SingletonPlugin())
            .with(EventSystem())
            .with(InputSystem())
            .with(ConnectionManager())
            .with(CollisionSystem())
            .with(ShopSystem())
            .with(RotatorSystem())
            .with(EntitySelectionSystem())
            .with(EntityClickSystem())
            .with(MinerConnectionSystem())
            .with(CameraPanZoomSystem())
            .with(MinerPlacementSystem())
            .with(MiningSystem())
            .withRender()
            .withUI()
            .with(TestingWorldSetupSystem())
            .build()
            .register(assetManager)
            .register(Stage())
    )

    override fun resize(width: Int, height: Int) {
        world.getSystem(EventSystem::class.java)?.dispatch(ResizeEvent(width, height))
    }

    override fun hide() {
        world.getSystem(EventSystem::class.java)?.dispatch(HideEvent())
    }

    override fun pause() {
        world.getSystem(EventSystem::class.java)?.dispatch(PauseEvent())
    }

    override fun resume() {
        world.getSystem(EventSystem::class.java)?.dispatch(ResumeEvent())
    }

    override fun show() {
        world.getSystem(EventSystem::class.java)?.dispatch(ShowEvent())
    }

    override fun dispose() {
        world.getSystem(EventSystem::class.java)?.dispatch(DisposeEvent())
        world.dispose()
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        world.setDelta(min(delta, MAX_DELTA));
        world.process()
    }
}