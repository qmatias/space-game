package com.spacegame.systems

import com.artemis.BaseSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.spacegame.events.*
import net.mostlyoriginal.api.event.common.EventSystem


class InputSystem : BaseSystem(), InputProcessor {
    private lateinit var eventSystem: EventSystem
    override fun processSystem() {}

    override fun initialize() {
        Gdx.input.inputProcessor = this
    }

    override fun keyDown(keycode: Int): Boolean {
        eventSystem.dispatch(KeyDownEvent(keycode))
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        eventSystem.dispatch(KeyUpEvent(keycode))
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        eventSystem.dispatch(KeyTypedEvent(character))
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        eventSystem.dispatch(TouchDownEvent(screenX, screenY, pointer, button))
        return false
    }


    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        eventSystem.dispatch(TouchUpEvent(screenX, screenY, pointer, button))
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        eventSystem.dispatch(TouchDraggedEvent(screenX, screenY, pointer))
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        eventSystem.dispatch(MouseMovedEvent(screenX, screenY))
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        eventSystem.dispatch(ScrollEvent(amountX, amountY))
        return false
    }
}