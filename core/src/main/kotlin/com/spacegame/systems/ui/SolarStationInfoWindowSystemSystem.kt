package com.spacegame.systems.ui

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.Assets
import com.spacegame.components.*
import com.spacegame.ui.*
import com.spacegame.util.getNullable

@All(SolarStation::class, Selected::class)
class SolarStationInfoWindowSystemSystem : SingleEntityInfoWindowSystem() {
    private lateinit var constructionWidget: ConstructionWidget
    private lateinit var healthWidget: HealthWidget
    private lateinit var batteryWidget: BatteryWidget
    private lateinit var energyProducedLabel: UpdatingLabel

    private lateinit var constructionMapper: ComponentMapper<Construction>
    private lateinit var healthMapper: ComponentMapper<Health>
    private lateinit var energyGeneratorMapper: ComponentMapper<EnergyGenerator>
    private lateinit var energyStorerMapper: ComponentMapper<EnergyStorer>

    @Wire
    private lateinit var assetManager: Assets

    private lateinit var skin: Skin

    private val tablePadding: Float = 10f
    private val tableSpacing: Float = 20f

    override fun initialize() {
        skin = assetManager.get(Assets.UI_SKIN)

        constructionWidget = ConstructionWidget(skin)
        healthWidget = HealthWidget(skin)
        batteryWidget = BatteryWidget(skin)
        energyProducedLabel = UpdatingLabel("Energy Produced: {}", skin)

        window = buildWindow()
    }

    fun buildWindow() =
        GameWindow("Solar Station I", skin).apply {
            add(Table(skin).apply {
                defaults().left().top().expand().fill().space(tableSpacing)
                add(constructionWidget)
                add(Label("Solar Station", skin)).row()
                add(healthWidget)
                add(energyProducedLabel).row()
                add(batteryWidget)
                add(Label("Solar Station", skin)).row()
            }).pad(tablePadding).expand().fill()
        }

    override fun updateWindowFor(e: Int) {
        val progress = constructionMapper.getNullable(e)?.progress ?: 1f
        val health = healthMapper.getNullable(e)
        val battery = energyStorerMapper.getNullable(e)
        val energyProduced = energyGeneratorMapper.getNullable(e)?.energyProduced ?: 0f

        constructionWidget.update(progress)
        healthWidget.update(health)
        batteryWidget.update(battery)
        energyProducedLabel.update(energyProduced)
    }

    override fun clearWindow() {
        constructionWidget.update()
        healthWidget.update()
        batteryWidget.update()
        energyProducedLabel.update()
    }
}