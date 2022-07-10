package com.spacegame

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import net.dermetfan.gdx.assets.AnnotationAssetManager

class Main : KtxGame<KtxScreen>() {
    val assetManager = Assets()

    override fun create() {
        KtxAsync.initiate()

        assetManager.loadAll()

        addScreen(FirstScreen(assetManager))
        setScreen<FirstScreen>()
    }
}

