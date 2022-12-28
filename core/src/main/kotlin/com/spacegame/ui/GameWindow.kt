package com.spacegame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.txt

class GameWindow(initialTitle: String, skin: Skin) : Table(skin) {
    private val titleLabel = Label(initialTitle, skin, "font", "black")
    var title = titleLabel::txt
    init {
        background = skin.getDrawable("window")

        // override background padding, we want the label to show on titlebar
        padTop(8f)
        add(titleLabel).expandX().padBottom(4f).row()
    }
}

