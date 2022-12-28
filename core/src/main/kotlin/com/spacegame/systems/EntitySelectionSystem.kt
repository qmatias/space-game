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

    private fun clearSelection() {
        for (e in entityIds) {
            selectedMapper.remove(e)
        }
    }

    @Subscribe(ignoreCancelledEvents = true)
    fun onEntityClick(ev: EntityClickEvent) {
        clearSelection()
        if (ev.entity !in entityIds) return // only affect selectable entities
        selectedMapper.add(ev.entity)
    }

    @Subscribe(ignoreCancelledEvents = true)
    fun onEntityClickMiss(_ev: EntityClickMissEvent) {
        clearSelection()
    }
}