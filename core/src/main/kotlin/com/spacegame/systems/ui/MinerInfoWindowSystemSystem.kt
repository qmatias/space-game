package com.spacegame.systems.ui

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.Assets
import com.spacegame.components.*
import com.spacegame.systems.ConnectionManager
import com.spacegame.ui.ConstructionWidget
import com.spacegame.ui.GameWindow
import com.spacegame.ui.HealthWidget
import com.spacegame.ui.UpdatingLabel
import com.spacegame.util.getNullable

@All(Miner::class, Selected::class)
class MinerInfoWindowSystemSystem : SingleEntityInfoWindowSystem() {
    private lateinit var constructionWidget: ConstructionWidget
    private lateinit var healthWidget: HealthWidget
    private lateinit var asteroidCountLabel: UpdatingLabel
    private lateinit var mpsLabel: UpdatingLabel
    private lateinit var totalMinedLabel: UpdatingLabel

    private lateinit var asteroidMapper: ComponentMapper<Asteroid>
    private lateinit var minerMapper: ComponentMapper<Miner>
    private lateinit var constructionMapper: ComponentMapper<Construction>
    private lateinit var healthMapper: ComponentMapper<Health>

    private lateinit var connectionManager: ConnectionManager

    @Wire
    private lateinit var assetManager: Assets

    private lateinit var skin: Skin

    private val tablePadding: Float = 10f
    private val tableSpacing: Float = 20f

    override fun initialize() {
        skin = assetManager.get(Assets.UI_SKIN)

        constructionWidget = ConstructionWidget(skin)
        healthWidget = HealthWidget(skin)
        asteroidCountLabel = UpdatingLabel("Asteroid Count: {}", skin)
        mpsLabel = UpdatingLabel("Minerals / Second: {}", skin)
        totalMinedLabel = UpdatingLabel("Minerals Mined: {}", skin)

        window = buildWindow()
    }

    fun buildWindow() =
        GameWindow("Miner Info", skin).apply {
            add(Table(skin).apply {
                defaults().left().top().expand().fill().space(tableSpacing)
                add(constructionWidget)
                add(mpsLabel).row()
                add(healthWidget)
                add(totalMinedLabel).row()
                add(asteroidCountLabel)
                add(Label("Miner", skin)).row()
            }).pad(tablePadding).expand().fill()
        }

    override fun updateWindowFor(e: Int) {
        val miner = minerMapper.get(e)
        val asteroids = connectionManager.get(e).filter { asteroidMapper.has(it) }
        val totalMps = asteroids.sumOf { asteroidMapper.get(it).mineralsPerSecond }
        val progress = constructionMapper.getNullable(e)?.progress ?: 1f
        val health = healthMapper.getNullable(e)

        constructionWidget.update(progress)
        healthWidget.update(health)
        asteroidCountLabel.update(asteroids.size)
        mpsLabel.update(totalMps)
        totalMinedLabel.update(miner.mineralsMined)
    }

    override fun clearWindow() {
        constructionWidget.update()
        asteroidCountLabel.update()
        mpsLabel.update()
        totalMinedLabel.update()
    }
}