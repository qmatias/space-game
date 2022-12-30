package com.spacegame.systems.ui

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.Assets
import com.spacegame.components.*
import com.spacegame.systems.ConnectionManager
import com.spacegame.ui.GameWindow
import com.spacegame.ui.UpdatingLabel
import com.spacegame.util.getNullable

@All(SolarStation::class, Selected::class)
class SolarStationInfoWindowSystemSystem(
    private val tablePadding: Float = 10f,
) : SingleEntityInfoWindowSystem() {
    private lateinit var energyLabel: UpdatingLabel
    private lateinit var capacityLabel: UpdatingLabel
    private lateinit var energyPerSecondLabel: UpdatingLabel

    private lateinit var generatorMapper: ComponentMapper<Generator>
    private lateinit var batteryMapper: ComponentMapper<Battery>

    private lateinit var connectionManager: ConnectionManager

    @Wire
    private lateinit var assetManager: Assets

    override fun initialize() {
        val skin = assetManager.get(Assets.UI_SKIN)

        window = GameWindow("Miner Info", skin)

        capacityLabel = UpdatingLabel("Energy: {} / {}", skin)
        energyPerSecondLabel = UpdatingLabel("Energy Production: {}", skin)

        val inner = Table()
        inner.top().defaults().left().expandX().fill()
        inner.add(capacityLabel).row()
        inner.add(energyPerSecondLabel).row()
        window.add(inner).pad(tablePadding).left().expand().fill()
    }

    override fun updateWindowFor(e: Int) {
        val generator = generatorMapper.getNullable(e)
        val battery = batteryMapper.getNullable(e)

        capacityLabel.update(battery?.energy ?: 0, battery?.capacity ?: 0)
        energyPerSecondLabel.update(generator?.energyPerSecond ?: 0)
    }

    override fun clearWindow() {
        capacityLabel.update()
        energyPerSecondLabel.update()
    }
}