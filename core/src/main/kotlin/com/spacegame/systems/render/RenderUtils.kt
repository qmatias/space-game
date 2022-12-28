package com.spacegame.systems.render

import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import space.earlygrey.shapedrawer.ShapeDrawer

fun WorldConfigurationBuilder.withRender(): WorldConfigurationBuilder {
    with(MinerConnectionRenderSystem())
    with(AsteroidRenderSystem())
    with(MinerRenderSystem())
    with(HighlightRenderSystem())
    with(RangeRenderSystem())
    return this
}

fun buildShapeDrawer(): ShapeDrawer {
    val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(Color.WHITE);
    pixmap.drawPixel(0, 0);
    val texture = Texture(pixmap)
    pixmap.dispose();
    val region = TextureRegion(texture, 0, 0, 1, 1);
    return ShapeDrawer(PolygonSpriteBatch(), region)
}