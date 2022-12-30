package com.spacegame.systems

import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.spacegame.components.Highlight
import com.spacegame.components.ShowRange
import com.spacegame.components.Selectable
import com.spacegame.components.Selected
import com.spacegame.events.EntityClickEvent
import com.spacegame.events.EntityClickMissEvent
import com.spacegame.util.*
import net.mostlyoriginal.api.event.common.Subscribe

@All(Selectable::class)
class EntitySelectionSystem : BaseEntitySystem() {
    private lateinit var selectedMapper: ComponentMapper<Selected>

    override fun processSystem() {}

    @Subscribe(ignoreCancelledEvents = true)
    fun onEntityClick(ev: EntityClickEvent) {
        for (e in entityIds) {
            if (e == ev.entity) {
                selectedMapper.toggle(ev.entity)
            } else {
                selectedMapper.remove(e)
            }
        }
    }

    @Subscribe(ignoreCancelledEvents = true)
    fun onEntityClickMiss(_ev: EntityClickMissEvent) {
        entityIds.iterator().forEach(selectedMapper::remove)
    }
}