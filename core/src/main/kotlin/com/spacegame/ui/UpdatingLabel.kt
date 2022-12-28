package com.spacegame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

const val PLACEHOLDER = "{}"

class UpdatingLabel(private val formatString: String, skin: Skin, vararg initialArgs: Any?) : Label("", skin) {
    private var currentArgs = initialArgs
    init {
        update(*initialArgs)
    }

    fun update(vararg args: Any?) {
        if (args.contentEquals(currentArgs)) return
        currentArgs = args

        var newText = formatString
        var replacements = 0
        while (PLACEHOLDER in newText) {
            newText = newText.replaceFirst(PLACEHOLDER, args.getOrNull(replacements)?.toString() ?: "")
            replacements++
        }
        setText(newText)
    }
}