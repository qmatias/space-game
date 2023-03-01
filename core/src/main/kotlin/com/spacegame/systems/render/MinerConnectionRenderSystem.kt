package com.spacegame.systems.render

import com.artemis.annotations.All
import com.badlogic.gdx.graphics.Color
import com.spacegame.components.AsteroidMiner
import com.spacegame.systems.connections.AsteroidMinerConnectionSystem
import com.spacegame.systems.connections.GenericConnectionSystem

@All(AsteroidMiner::class)
class MinerConnectionRenderSystem : GenericConnectionRenderSystem<AsteroidMinerConnectionSystem>() {
    override lateinit var connectionSystem: AsteroidMinerConnectionSystem

    override fun drawConnection(from: Int, to: Int) {
        drawLine(from, to, Color.BLUE)
    }
}
