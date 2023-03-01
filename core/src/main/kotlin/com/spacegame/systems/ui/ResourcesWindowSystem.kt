package com.spacegame.systems.ui

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.Assets
import com.spacegame.components.Resources
import com.spacegame.systems.BatterySystem
import com.spacegame.systems.MiningSystem
import com.spacegame.ui.BatteryWidget
import com.spacegame.ui.GameWindow
import com.spacegame.ui.UpdatingLabel

class ResourcesWindowSystem : BaseSystem() {
    private lateinit var miningSystem: MiningSystem
    private lateinit var batterySystem: BatterySystem
    private lateinit var resources: Resources

    private lateinit var mineralLabel: UpdatingLabel
    private lateinit var mpmLabel: UpdatingLabel
    private lateinit var batteryWidget: BatteryWidget

    @Wire
    private lateinit var assetManager: Assets

    private lateinit var skin: Skin

    lateinit var window: Table

    private val tablePadding: Float = 10f
    private val resourceSpacing: Float = 10f

    override fun initialize() {
        skin = assetManager.get(Assets.UI_SKIN)

        mineralLabel = UpdatingLabel("Minerals: {}", skin)
        mpmLabel = UpdatingLabel("{} Minerals per Minute", skin)
        batteryWidget = BatteryWidget(skin)

        window = buildWindow()
    }

    fun buildWindow() = GameWindow("Resources", skin).apply {
        add(Table(skin).apply {
            defaults().left().top().space(resourceSpacing)
            add(Table(skin).apply {
                add(mineralLabel).row()
                add(mpmLabel).row()
            }).row()
            add(batteryWidget)
        }).pad(tablePadding).grow()
    }

    override fun processSystem() {
        mineralLabel.update(resources.minerals)
        mpmLabel.update(miningSystem.mineralsPerSecond * 60)
        batteryWidget.update(batterySystem.totalEnergyStored, batterySystem.totalEnergyCapacity)
    }
}
