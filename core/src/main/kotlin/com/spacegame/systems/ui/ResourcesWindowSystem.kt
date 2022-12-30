package com.spacegame.systems.ui

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.Assets
import com.spacegame.components.Resources
import com.spacegame.systems.BatterySystem
import com.spacegame.systems.GeneratorSystem
import com.spacegame.systems.MiningSystem
import com.spacegame.ui.GameWindow
import com.spacegame.ui.UpdatingLabel

class ResourcesWindowSystem(
    private val tablePadding: Float = 10f,
) : BaseSystem() {
    private lateinit var miningSystem: MiningSystem

    private lateinit var resources: Resources

    private lateinit var mineralLabel: UpdatingLabel
    private lateinit var mpmLabel: UpdatingLabel
    private lateinit var shipLabel: UpdatingLabel
    private lateinit var batteryLabel: UpdatingLabel
    private lateinit var energyPerSecondLabel: UpdatingLabel

    private lateinit var batterySystem: BatterySystem
    private lateinit var generatorSystem: GeneratorSystem

    @Wire
    private lateinit var assetManager: Assets

    lateinit var window: Table

    override fun initialize() {
        val skin = assetManager.get(Assets.UI_SKIN)

        mineralLabel = UpdatingLabel("Minerals: {}", skin)
        mpmLabel = UpdatingLabel("{} Minerals per Minute", skin)
        batteryLabel = UpdatingLabel("Total Energy: {} / {}", skin)
        energyPerSecondLabel = UpdatingLabel("Energy Production: {}", skin)
        shipLabel = UpdatingLabel("Ships: {}", skin)

        window = GameWindow("Resources", skin)

        val inner = Table()
        inner.top().defaults().left().expandX().fill()
        inner.add(mineralLabel).row()
        inner.add(mpmLabel).row()
        inner.add(batteryLabel).row()
        inner.add(energyPerSecondLabel).row()
        inner.add(shipLabel).row()

        window.add(inner).pad(tablePadding).expand().fill()
    }

    override fun processSystem() {
        mineralLabel.update(resources.minerals)
        mpmLabel.update(miningSystem.mineralsPerSecond * 60)
        shipLabel.update(resources.ships)
        batteryLabel.update(batterySystem.totalEnergyStored, batterySystem.totalEnergyCapacity)
        energyPerSecondLabel.update(generatorSystem.maxEnergyProduction)
    }
}
