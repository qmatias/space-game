package com.spacegame.systems

import com.artemis.Component
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.spacegame.components.*
import com.spacegame.util.getNullable
import com.spacegame.util.sequence
import net.mostlyoriginal.api.component.Schedule
import net.mostlyoriginal.api.operation.OperationFactory.*
import net.mostlyoriginal.api.utils.Duration.milliseconds

@All(Selected::class, Spinning::class, Rotation::class)
class SpinSelectedEntitySystem : IteratingSystem() {
    private lateinit var scheduleMapper: ComponentMapper<Schedule>
    private lateinit var rotationMapper: ComponentMapper<Rotation>
    private lateinit var savedRotationMapper: ComponentMapper<SavedRotation>

    override fun process(e: Int) {}

    override fun inserted(e: Int) {
        val rotation = rotationMapper.get(e).rotation
        scheduleMapper.create(e).set(
            add(SavedRotation(rotation)),
            tween(Rotation(rotation), Rotation(0f), milliseconds(150f))
        )
    }

    override fun removed(e: Int) {
        val rotation = savedRotationMapper.getNullable(e)?.rotation ?: 0f
        scheduleMapper.create(e).set(
            tween(Rotation(0f), Rotation(rotation), milliseconds(150f)),
            remove(SavedRotation::class.java)
        )
    }
}