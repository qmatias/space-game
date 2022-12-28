package com.spacegame.events

import net.mostlyoriginal.api.event.common.Cancellable
import net.mostlyoriginal.api.event.common.Event

abstract class CancellableEvent : Event, Cancellable {
    private var cancelled = false
    override fun isCancelled(): Boolean = cancelled
    override fun setCancelled(value: Boolean) {
        cancelled = value
    }
}