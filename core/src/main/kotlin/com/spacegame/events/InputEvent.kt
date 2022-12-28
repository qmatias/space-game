package com.spacegame.events

import com.badlogic.gdx.InputProcessor
import net.mostlyoriginal.api.event.common.Cancellable
import net.mostlyoriginal.api.event.common.Event

abstract class InputEvent : CancellableEvent() {
    fun forwardToInputProcessor(processor: InputProcessor) =
        when (this) {
            is KeyDownEvent -> processor.keyDown(keycode)
            is KeyUpEvent -> processor.keyUp(keycode)
            is KeyTypedEvent -> processor.keyTyped(character)
            is TouchDownEvent -> processor.touchDown(screenX, screenY, pointer, button)
            is TouchUpEvent -> processor.touchUp(screenX, screenY, pointer, button)
            is TouchDraggedEvent -> processor.touchDragged(screenX, screenY, pointer)
            is MouseMovedEvent -> processor.mouseMoved(screenX, screenY)
            is ScrollEvent -> processor.scrolled(amountX, amountY)
            else -> false
        }
}

class KeyDownEvent(val keycode: Int) : InputEvent()
class KeyUpEvent(val keycode: Int) : InputEvent()
class KeyTypedEvent(val character: Char) : InputEvent()
class TouchDownEvent(val screenX: Int, val screenY: Int, val pointer: Int, val button: Int) : InputEvent()
class TouchUpEvent(val screenX: Int, val screenY: Int, val pointer: Int, val button: Int) : InputEvent()
class TouchDraggedEvent(val screenX: Int, val screenY: Int, val pointer: Int) : InputEvent()
class MouseMovedEvent(val screenX: Int, val screenY: Int) : InputEvent()
class ScrollEvent(val amountX: Float, val amountY: Float) : InputEvent()

