package com.spacegame.systems

import com.artemis.BaseSystem
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.spacegame.events.ScrollEvent
import com.spacegame.events.TouchDownEvent
import com.spacegame.events.TouchDraggedEvent
import com.spacegame.components.Camera
import com.spacegame.events.KeyTypedEvent
import net.mostlyoriginal.api.event.common.Subscribe


class CameraPanZoomSystem(
    private val zoomInterval: Float = 0.05f
) : BaseSystem() {
    private lateinit var camera: Camera
    private val lastTouch = Vector2()

    override fun initialize() {
        camera.camera.zoom = 0.5f
        camera.camera.update()
    }

    override fun processSystem() {}

    @Subscribe
    fun onTouchDown(e: TouchDownEvent) {
        lastTouch.set(e.screenX.toFloat(), e.screenY.toFloat())
    }

    @Subscribe(ignoreCancelledEvents = true, priority = 100)
    fun onTouchDragged(e: TouchDraggedEvent) {
        val movement = Vector2(e.screenX.toFloat(), e.screenY.toFloat())
            .sub(lastTouch)
            .scl(-1f, 1f)
            .scl(camera.camera.zoom)

        camera.camera.translate(movement)
        camera.camera.update()

        lastTouch.set(e.screenX.toFloat(), e.screenY.toFloat())

        e.isCancelled = true
    }

    @Subscribe(ignoreCancelledEvents = true, priority = 100)
    fun onScroll(e: ScrollEvent) {
        camera.camera.zoom *= 1 + e.amountY * zoomInterval
        camera.camera.update()

        e.isCancelled = true
    }

    @Subscribe(ignoreCancelledEvents = true, priority = 100)
    fun onKeyTyped(e: KeyTypedEvent) {
        when (e.character) {
            'r' -> camera.camera.zoom = 1f
            '+' -> camera.camera.zoom -= 1
            '-' -> camera.camera.zoom += 1
            else -> return
        }
        camera.camera.update()
        e.isCancelled = true
    }
}