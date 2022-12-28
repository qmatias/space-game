package com.spacegame.events

import com.badlogic.gdx.InputProcessor
import net.mostlyoriginal.api.event.common.Cancellable
import net.mostlyoriginal.api.event.common.Event

abstract class LifecycleEvent : Event

class ResizeEvent(val width: Int, val height: Int) : LifecycleEvent()
class HideEvent : LifecycleEvent()
class PauseEvent : LifecycleEvent()
class ResumeEvent : LifecycleEvent()
class ShowEvent : LifecycleEvent()
class DisposeEvent : LifecycleEvent()