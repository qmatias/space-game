package com.spacegame.systems.ui

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Scaling
import com.spacegame.Assets
import com.spacegame.Structure
import com.spacegame.events.PurchaseStructureEvent
import com.spacegame.ui.GameWindow
import ktx.actors.onChange
import ktx.actors.onClick
import net.mostlyoriginal.api.event.common.EventSystem

class ShopWindowSystem : BaseSystem() {
    private val iconPadding: Float = 5f
    private val itemSize: Float = 130f
    private val buttonSpacing: Float = 10f
    private val tablePadding: Float = 15f

    lateinit var window: Table

    private lateinit var eventSystem: EventSystem

    @Wire
    private lateinit var assetManager: Assets

    private lateinit var skin: Skin
    private lateinit var shopSectionCell: Cell<Table>

    private var sectionIdx = 0
    private val sectionSize = 4
    private val sections = Structure.values().toList().chunked(sectionSize)

    override fun initialize() {
        skin = assetManager.get(Assets.UI_SKIN)

        window = buildGameWindow()
    }

    fun rebuildShopSection() {
        shopSectionCell.setActor(buildShopSection())
    }

    fun buildGameWindow(): GameWindow =
        GameWindow("Shop", skin).apply {
            add(Table(skin).apply {
                defaults().space(buttonSpacing)
                add(ImageButton(skin, "left").apply {
                    onChange {
                        sectionIdx = Math.floorMod(sectionIdx - 1, sections.size)
                        rebuildShopSection()
                    }
                })
                shopSectionCell = add(buildShopSection()).fill().expand()
                add(ImageButton(skin, "right").apply {
                    onChange {
                        sectionIdx = Math.floorMod(sectionIdx + 1, sections.size)
                        rebuildShopSection()
                    }
                })
            }).fill().expand().pad(tablePadding)
        }


    fun buildShopSection(): Table =
        Table(skin).apply {
            row().width(itemSize).height(itemSize).expand()
            sections[sectionIdx].forEach { structure ->
                add(buildStructureContainer(structure))
            }
        }

    fun buildStructureContainer(structure: Structure) =
        Table(skin).apply {
            val texture = assetManager.get(structure.icon)
            add(Image(texture).apply {
                setScaling(Scaling.contain)
                onClick { eventSystem.dispatch(PurchaseStructureEvent(structure)) }
            }).fill().expand().pad(iconPadding)
            setBackground("progress-bar-back")
        }

    override fun processSystem() {}
}
