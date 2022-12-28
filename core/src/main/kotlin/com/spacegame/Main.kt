package com.spacegame

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class Main : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        val assetManager = Assets()
        assetManager.loadAll()
        addScreen(GameScreen(assetManager))
        setScreen<GameScreen>()
    }
}

