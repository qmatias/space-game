package com.spacegame.systems.ui

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.Assets
import com.spacegame.components.Resources
import com.spacegame.systems.MiningSystem
import com.spacegame.ui.GameWindow
import com.spacegame.ui.UpdatingLabel

class ResourcesWindowSystem(
    private val tablePadding: Float = 10f,
) : BaseSystem() {
    private lateinit var miningSystem: MiningSystem

    private lateinit var resources: Resources

    private lateinit var mineralLabel: UpdatingLabel
    private lateinit var mpsLabel: UpdatingLabel
    private lateinit var shipLabel: UpdatingLabel

    @Wire
    private lateinit var assetManager: Assets

    lateinit var window: Table

    override fun initialize() {
        val skin = assetManager.get(Assets.UI_SKIN)

        mineralLabel = UpdatingLabel("Minerals: {}", skin)
        mpsLabel = UpdatingLabel("Minerals / Second: {}", skin)
        shipLabel = UpdatingLabel("Ships: {}", skin)

        window = GameWindow("Resources", skin)

        val inner = Table()
        inner.top().defaults().left().expandX().fill()
        inner.add(mineralLabel).row()
        inner.add(mpsLabel).row()
        inner.add(shipLabel).row()

        window.add(inner).pad(tablePadding).expand().fill()
    }

    override fun processSystem() {
        mineralLabel.update(resources.minerals)
        mpsLabel.update(miningSystem.mineralsPerSecond)
        if (resources.ships <= 0) {
            shipLabel.isVisible = false
        } else {
            shipLabel.isVisible = true
            shipLabel.update(resources.ships)
        }
    }
}
