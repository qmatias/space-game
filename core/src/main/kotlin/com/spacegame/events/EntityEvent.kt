package com.spacegame.events

import net.mostlyoriginal.api.event.common.Event

abstract class EntityEvent : Event

class EntityClickEvent(val entity: Int) : EntityEvent()
class EntityClickMissEvent : EntityEvent()
