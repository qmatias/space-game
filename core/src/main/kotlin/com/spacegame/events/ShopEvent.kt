package com.spacegame.events

import com.spacegame.Structure
import net.mostlyoriginal.api.event.common.Event

abstract class ShopEvent : Event

class PurchaseStructureEvent(val structure: Structure) : ShopEvent()