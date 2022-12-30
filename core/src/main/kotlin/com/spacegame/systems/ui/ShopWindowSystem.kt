package com.spacegame.systems.ui

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
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
    private val iconPadding: Float = 5f,
    private val itemSize: Float = 130f,
    private val buttonSpacing: Float = 10f,
    private val tablePadding: Float = 15f
) : BaseSystem() {
    lateinit var window: Table

    lateinit var eventSystem: EventSystem

    @Wire
    lateinit var assetManager: Assets

    override fun initialize() {
        val skin = assetManager.get(Assets.UI_SKIN)

        window = GameWindow("Shop", skin)
        val gallery = Table()
        gallery.row().width(itemSize).height(itemSize).expand()

        for (structure in Structure.values()) {
            val texture = assetManager.get(structure.icon)
            val image = Image(texture)
            val container = Table(skin)
            image.setScaling(Scaling.contain)
            image.onClick {
                eventSystem.dispatch(PurchaseStructureEvent(structure))
            }
            container.add(image).fill().expand().pad(iconPadding)
            container.setBackground("progress-bar-back")
            gallery.add(container)
        }

        gallery.skin = skin
        val inner = Table()
        inner.defaults().space(buttonSpacing)
        inner.add(ImageButton(skin, "left"))
        inner.add(gallery).fill().expand()
        inner.add(ImageButton(skin, "right"))
        window.add(inner).fill().expand().pad(tablePadding)
    }

    override fun processSystem() {}
}
