package com.spacegame.systems

import com.artemis.BaseSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.input.GestureDetector.GestureAdapter
import com.spacegame.components.Camera


class InputSystem : BaseSystem() {
    lateinit var camera: Camera
    var currentZoom = 1f

    fun registerInputProcessor() {
        Gdx.input.inputProcessor = InputMultiplexer(
            MobileGestureHandler(),
            DesktopGestureHandler()
        )
    }

    override fun processSystem() { }

    inner class MobileGestureHandler() : GestureAdapter() {
        override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
            camera.camera.translate(-deltaX * currentZoom, deltaY * currentZoom)
            camera.camera.update()
            return false
        }

        override fun zoom(initialDistance: Float, distance: Float): Boolean {
            camera.camera.zoom = initialDistance / distance * currentZoom
            camera.camera.update()
            return true
        }

        override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
            Gdx.app.log("INFO", "panStop")
            currentZoom = camera.camera.zoom
            return false
        }
    }

    inner class DesktopGestureHandler() : InputAdapter() {

    }
}