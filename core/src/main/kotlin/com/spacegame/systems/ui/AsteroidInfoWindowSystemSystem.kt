package com.spacegame.systems.ui

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.Wire
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.spacegame.Assets
import com.spacegame.components.*
import com.spacegame.ui.GameWindow
import com.spacegame.ui.UpdatingLabel
import com.spacegame.util.getNullable
import kotlin.math.roundToInt

@All(Selected::class, Asteroid::class)
class AsteroidInfoWindowSystemSystem(
    private val tablePadding: Float = 10f,
) : SingleEntityInfoWindowSystem() {
    private lateinit var sizeLabel: UpdatingLabel
    private lateinit var mpsLabel: UpdatingLabel

    private lateinit var asteroidMapper: ComponentMapper<Asteroid>
    private lateinit var sizeMapper: ComponentMapper<Size>

    @Wire
    private lateinit var assetManager: Assets

    override fun initialize() {
        val skin = assetManager.get(Assets.UI_SKIN)

        sizeLabel = UpdatingLabel("Asteroid Size: {}", skin)
        mpsLabel = UpdatingLabel("Minerals / Second: {}", skin)

        window = GameWindow("Asteroid Info", skin)

        val inner = Table()
        inner.top().defaults().left().expandX().fill()
        inner.add(sizeLabel).row()
        inner.add(mpsLabel).row()
        window.add(inner).pad(tablePadding).left().expand().fill()
    }

    override fun updateWindowFor(e: Int) {
        val mps = asteroidMapper.get(e).mineralsPerSecond
        val size = sizeMapper.getNullable(e)?.size ?: 0f

        sizeLabel.update(size.roundToInt())
        mpsLabel.update(mps)
    }

    override fun clearWindow() {
        sizeLabel.update()
        mpsLabel.update()
    }
}
