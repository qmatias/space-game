package com.spacegame.systems.ui

import com.artemis.BaseSystem
import com.artemis.WorldConfigurationBuilder
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.events.InputEvent
import com.spacegame.events.TouchDownEvent
import net.mostlyoriginal.api.event.common.Subscribe

fun WorldConfigurationBuilder.withUI(): WorldConfigurationBuilder {
    with(AsteroidInfoWindowSystemSystem())
    with(MinerInfoWindowSystemSystem())
    with(MapWindowSystem())
    with(ShopWindowSystem())
    with(ResourcesWindowSystem())
    with(UiSystem())
    return this
}

class UiSystem(
    private val edgeMargin: Float = 10f,
    private val mapSize: Float = 325f,
    private val shopHeight: Float = 200f,
    private val resWindowWidth: Float = 400f,
    private val resWindowHeight: Float = 300f,
) : BaseSystem() {
    private lateinit var mapWindowSystem: MapWindowSystem
    private lateinit var shopWindowSystem: ShopWindowSystem
    private lateinit var asteroidInfoWindowSystem: AsteroidInfoWindowSystemSystem
    private lateinit var minerInfoWindow: MinerInfoWindowSystemSystem
    private lateinit var resourcesWindowSystem: ResourcesWindowSystem

    private lateinit var bottomBarCell: Cell<Actor>

    @Wire
    private lateinit var stage: Stage

    override fun processSystem() {
        updateBottomBar()

        stage.act(world.delta)
        stage.draw()
    }

    private fun updateBottomBar() {
        if (minerInfoWindow.active) {
            bottomBarCell.setActor(minerInfoWindow.window)
        } else if (asteroidInfoWindowSystem.active) {
            bottomBarCell.setActor(asteroidInfoWindowSystem.window)
        } else {
            bottomBarCell.setActor(shopWindowSystem.window)
        }
    }

    override fun initialize() {
        // bottom bar
        stage.addActor(Table().also { bottomBar ->
            bottomBar.setFillParent(true)
            bottomBar.bottom()

            bottomBar.add(Table().also { inner ->
                inner.defaults().space(edgeMargin).bottom().fill()

                // add map window to bottom left
                inner.add(mapWindowSystem.window).width(mapSize).height(mapSize)

                // create bottomBarCell
                bottomBarCell = inner.add().expandX().height(shopHeight)

                // res window
                inner.add(resourcesWindowSystem.window).width(resWindowWidth).height(resWindowHeight)
            }).pad(edgeMargin).bottom().expandX().fill()
        })
    }

    @Subscribe(ignoreCancelledEvents = true, priority = 700)
    fun onInputEvent(e: InputEvent) {
        if (e.forwardToInputProcessor(stage))
            e.isCancelled = true
    }

    @Subscribe(priority = 600)
    fun onTouchDown(e: TouchDownEvent) {
        if (!e.isCancelled) stage.unfocusAll()
    }
}