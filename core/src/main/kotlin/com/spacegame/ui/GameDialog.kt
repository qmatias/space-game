package com.spacegame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class GameDialog(skin: Skin) : Dialog("", skin, "special") {
    override fun text(label: Label): Dialog {
        contentTable.add(label).row();
        return this;
    }
}