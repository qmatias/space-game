package com.spacegame.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.spacegame.components.*
import com.spacegame.events.TouchUpEvent
import com.spacegame.util.add
import com.spacegame.util.iterator
import com.spacegame.util.sequence
import net.mostlyoriginal.api.component.Schedule
import net.mostlyoriginal.api.event.common.Subscribe
import net.mostlyoriginal.api.operation.OperationFactory.*
import net.mostlyoriginal.api.utils.Duration.milliseconds

@All(BeingPlaced::class)
class MinerPlacementSystem : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>

    private lateinit var collisionSystem: CollisionSystem

    private lateinit var camera: Camera

    private var mousePos = Vector2()

    override fun begin() {
        val pose = camera.camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        mousePos.x = pose.x
        mousePos.y = pose.y
    }

    override fun inserted(e: Int) {
        world.edit(e)
            .add(ShowRange())
    }

    override fun removed(e: Int) {
        world.edit(e)
            .remove(BeingPlaced::class.java)
            .remove(ShowRange::class.java)
            .add(Active())
            .add(Selectable())
            .add(Collideable())
            .add(Spinning())
    }

    override fun process(e: Int) {
        // follow mouse
        val position = positionMapper.create(e)
        position.x = mousePos.x
        position.y = mousePos.y

    }

    @Subscribe(ignoreCancelledEvents = true)
    fun onTouchUp(ev: TouchUpEvent) {
        for (e in entityIds) {
            dropMiner(e)
            // cancel events while we're trying to drop something
            ev.isCancelled = true
        }
    }

    private fun dropMiner(miner: Int) {
        val colliding = collisionSystem.getOneColliding(miner)
        if (colliding != null)
            return flashCollidingEntity(colliding)

        world.edit(miner).remove(BeingPlaced::class.java)
    }

    private fun flashCollidingEntity(colliding: Int) {
        world.edit(colliding).add(Flashing())
    }
}