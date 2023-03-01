package com.spacegame.systems.render

import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import space.earlygrey.shapedrawer.ShapeDrawer

fun WorldConfigurationBuilder.withRender(): WorldConfigurationBuilder {
    with(SelectedRenderSystem())
    with(MinerConnectionRenderSystem())
    with(EnergyConnectionRenderSystem())
    with(RangeRenderSystem())
    with(TextureRenderSystem())
    with(HighlightRenderSystem())
    return this
}

fun buildShapeDrawer(batch: Batch): ShapeDrawer {
    val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(Color.WHITE);
    pixmap.drawPixel(0, 0);
    val texture = Texture(pixmap)
    pixmap.dispose();
    val region = TextureRegion(texture, 0, 0, 1, 1);
    return ShapeDrawer(batch, region)
}