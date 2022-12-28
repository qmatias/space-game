package com.spacegame.systems.ui

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Scaling
import com.spacegame.Assets
import com.spacegame.Structure
import com.spacegame.events.PurchaseStructureEvent
import com.spacegame.systems.ShopSystem
import com.spacegame.ui.GameDialog
import com.spacegame.ui.GameWindow
import ktx.actors.onChangeEvent
import ktx.actors.onClick
import net.mostlyoriginal.api.event.common.EventSystem

class ShopWindowSystem(
    val iconPadding: Float = 20f,
    val itemSize: Float = 100f,
    private val tablePadding: Float = 10f
) : BaseSystem() {
    lateinit var window: Table

    lateinit var eventSystem: EventSystem

    @Wire
    lateinit var assetManager: Assets

    @Wire
    lateinit var stage: Stage

    override fun initialize() {
        val skin = assetManager.get(Assets.UI_SKIN)

        window = GameWindow("Shop", skin)
        val gallery = Table()
        gallery.left().row().space(iconPadding).width(itemSize).height(itemSize)

        for (structure in Structure.values()) {
            val texture = assetManager.get(structure.icon)
            val image = Image(texture)
            val container = Table()
            image.setScaling(Scaling.contain)
            image.onClick {
                eventSystem.dispatch(PurchaseStructureEvent(structure))
            }
            container.add(image).fill().expand()
            gallery.add(container)
        }

        gallery.skin = skin
        gallery.setBackground("soldier")

        window.add(gallery).fill().expand().pad(tablePadding)
        window.setDebug(true, true)
    }

    override fun processSystem() {}
}
