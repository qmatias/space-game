package com.spacegame.systems

import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.spacegame.components.*
import com.spacegame.events.TouchUpEvent
import com.spacegame.util.add
import com.spacegame.util.iterator
import net.mostlyoriginal.api.event.common.Subscribe

@All(BeingPlaced::class)
class MinerPlacementSystem : IteratingSystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var beingPlacedMapper: ComponentMapper<BeingPlaced>
    private lateinit var showRangeMapper: ComponentMapper<ShowRange>
    private lateinit var selectableMapper: ComponentMapper<Selectable>
    private lateinit var collideableMapper: ComponentMapper<Collideable>

    private lateinit var collisionSystem: CollisionSystem

    private lateinit var camera: Camera

    private var mousePos = Vector2()

    override fun begin() {
        val pose = camera.camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        mousePos.x = pose.x
        mousePos.y = pose.y
    }

    override fun process(e: Int) {
        // follow mouse
        val position = positionMapper.create(e)
        position.x = mousePos.x
        position.y = mousePos.y

        // show range, don't allow selection
        showRangeMapper.add(e)
        selectableMapper.remove(e)
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
        if (collisionSystem.getOneColliding(miner) != null)
            return

        beingPlacedMapper.remove(miner)
        showRangeMapper.remove(miner)
        selectableMapper.add(miner)
        collideableMapper.add(miner)
    }
}