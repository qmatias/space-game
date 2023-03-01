package com.spacegame

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.spacegame.events.*
import com.spacegame.systems.*
import com.spacegame.systems.connections.withConnections
import com.spacegame.systems.render.*
import com.spacegame.systems.ui.*
import ktx.app.KtxScreen
import ktx.app.clearScreen
import net.mostlyoriginal.api.SingletonPlugin
import net.mostlyoriginal.api.event.common.EventSystem
import net.mostlyoriginal.plugin.OperationsPlugin

const val MAX_DELTA = 1 / 15f

class GameScreen(assetManager: Assets) : KtxScreen {
    private val batch = PolygonSpriteBatch()
    private val world = World(
        WorldConfigurationBuilder()
            .dependsOn(OperationsPlugin::class.java)
            .with(SingletonPlugin())
            .with(EventSystem())
            .with(InputSystem())
            .with(CollisionSystem())
            .with(ShopSystem())
            .with(RotatorSystem())
            .with(FlashSystem())
            .with(GeneratorSystem())
            .with(BatterySystem())
//            .with(SpinSelectedEntitySystem())
            .with(ConstructionSystem())
            .with(EntitySelectionSystem())
            .with(EntityClickSystem())
            .with(ConstructionRenderSystem())
            .with(CameraPanZoomSystem())
            .with(PlacementSystem())
            .with(MiningSystem())
            .withConnections()
            .withRender()
            .withUI()
            .with(ProcGenWorldSetupSystem())
            .build()
            .register(assetManager)
            .register(Stage())
            .register("batch", batch)
            .register(buildShapeDrawer(batch))
            .register(ShapeRenderer())
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
        clearScreen(red = 0.0f, green = 0.0f, blue = 0.0f)
        world.setDelta(minOf(delta, MAX_DELTA));
        world.process()
    }
}