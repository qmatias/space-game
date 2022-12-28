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
import com.spacegame.util.iterator
import com.spacegame.util.toList

@All(Miner::class, Selected::class)
class MinerInfoWindowSystemSystem(
    private val tablePadding: Float = 10f,
) : SingleEntityInfoWindowSystem() {
    private lateinit var minerLabel: UpdatingLabel
    private lateinit var mpsLabel: UpdatingLabel

    private lateinit var asteroidMapper: ComponentMapper<Asteroid>

    private lateinit var connectionManager: ConnectionManager

    @Wire
    private lateinit var assetManager: Assets

    override fun initialize() {
        val skin = assetManager.get(Assets.UI_SKIN)

        minerLabel = UpdatingLabel("Asteroid Count: {}", skin)
        mpsLabel = UpdatingLabel("Minerals / Second: {}", skin)

        window = GameWindow("Asteroid Miner Info", skin)

        val inner = Table()
        inner.top().defaults().left().expandX().fill()
        inner.add(minerLabel).row()
        inner.add(mpsLabel).row()
        window.add(inner).pad(tablePadding).left().expand().fill()
    }

    override fun updateWindowFor(e: Int) {
        val asteroids = connectionManager.getConnections(e).filter { asteroidMapper.has(it) }
        val totalMps = asteroids.sumOf { asteroidMapper.get(it).mineralsPerSecond }

        minerLabel.update(asteroids.size)
        mpsLabel.update(totalMps)
    }

    override fun clearWindow() {
        minerLabel.update()
        mpsLabel.update()
    }
}