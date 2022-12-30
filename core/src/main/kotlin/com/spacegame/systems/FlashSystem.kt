package com.spacegame.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.spacegame.components.*
import com.spacegame.util.sequence
import net.mostlyoriginal.api.component.Schedule
import net.mostlyoriginal.api.operation.OperationFactory.*
import net.mostlyoriginal.api.utils.Duration.milliseconds

@All(Flashing::class)
class FlashSystem : IteratingSystem() {
    private lateinit var flashingMapper: ComponentMapper<Flashing>
    private lateinit var scheduleMapper: ComponentMapper<Schedule>

    override fun process(e: Int) {}

    override fun inserted(e: Int) {
        val color = flashingMapper.get(e).color
        scheduleMapper.create(e).set(
            sequence(
                add(Highlight(color)),
                delay(milliseconds(200f)),

                remove(Highlight::class.java),
                delay(milliseconds(100f)),

                add(Highlight(color)),
                delay(milliseconds(200f)),

                remove(Highlight::class.java),
                remove(Flashing::class.java)
            )
        )
    }
}