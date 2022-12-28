package com.spacegame.systems

import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector3
import com.spacegame.events.TouchDownEvent
import com.spacegame.components.Camera
import com.spacegame.components.Position
import com.spacegame.components.Size
import com.spacegame.events.EntityClickEvent
import com.spacegame.events.EntityClickMissEvent
import com.spacegame.util.iterator
import net.mostlyoriginal.api.event.common.EventSystem
import net.mostlyoriginal.api.event.common.Subscribe

@All(Position::class, Size::class)
class EntityClickSystem : BaseEntitySystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var sizeMapper: ComponentMapper<Size>

    private lateinit var collisionSystem: CollisionSystem

    private lateinit var camera: Camera
    private lateinit var eventSystem: EventSystem
    override fun processSystem() {}

    private fun findEntityAt(screenX: Int, screenY: Int): List<Int> {
        val touch = camera.camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        val clicked = mutableListOf<Int>()
        for (e in entityIds) {
            val bb = collisionSystem.getEntityBB(e)
            if (bb.pointCollides(touch.x, touch.y))
                clicked.add(e)
        }
        return clicked
    }

    @Subscribe(ignoreCancelledEvents = true, priority = 300)
    fun onTouchDown(ev: TouchDownEvent) {
        if (ev.button != Input.Buttons.LEFT) return

        val clicked = findEntityAt(ev.screenX, ev.screenY)

        if (clicked.isEmpty())
            return eventSystem.dispatch(EntityClickMissEvent())

        // TODO sort by z axis when we add that
        ev.isCancelled = true
        for (e in clicked) {
            eventSystem.dispatch(EntityClickEvent(e))
        }
    }
}