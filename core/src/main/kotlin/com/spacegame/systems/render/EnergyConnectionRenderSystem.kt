package com.spacegame.systems.render

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.badlogic.gdx.graphics.Color
import com.spacegame.components.Position
import com.spacegame.components.Powerline
import com.spacegame.systems.connections.AsteroidMinerConnectionSystem
import com.spacegame.systems.connections.EnergyConnectionSystem
import com.spacegame.systems.connections.GenericConnectionSystem

@All(Powerline::class)
class EnergyConnectionRenderSystem : GenericConnectionRenderSystem<EnergyConnectionSystem>() {
    private lateinit var positionMapper: ComponentMapper<Position>

    override lateinit var connectionSystem: EnergyConnectionSystem

    override fun drawConnection(from: Int, to: Int) {
        drawLine(from, to, Color.MAGENTA)
    }
}
