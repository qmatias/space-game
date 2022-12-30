package com.spacegame.systems

import com.artemis.BaseEntitySystem
import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.badlogic.gdx.math.Vector2
import com.spacegame.components.Collideable
import com.spacegame.components.Position
import com.spacegame.components.Range
import com.spacegame.components.Size
import com.spacegame.util.getNullable
import com.spacegame.util.iterator

interface BoundingBox {
    fun pointCollides(px: Float, py: Float): Boolean
    fun collides(bb: BoundingBox): Boolean
}

class NullBoundingBox : BoundingBox {
    override fun pointCollides(px: Float, py: Float): Boolean = false
    override fun collides(bb: BoundingBox): Boolean = false
}

class CircularBoundingBox(val x: Float, val y: Float, val radius: Float) : BoundingBox {
    private fun center(): Vector2 = Vector2(x, y)

    override fun pointCollides(px: Float, py: Float): Boolean =
        pointCollidesCircle(px, py, x, y, radius)

    override fun collides(bb: BoundingBox): Boolean =
        when (bb) {
            is CircularBoundingBox -> circleCollidesCircle(x, y, radius, bb.x, bb.y, bb.radius)
            is NullBoundingBox -> false
            else -> throw NotImplementedError()
        }
}

@All(Collideable::class)
class CollisionSystem : BaseEntitySystem() {
    private lateinit var positionMapper: ComponentMapper<Position>
    private lateinit var sizeMapper: ComponentMapper<Size>
    private lateinit var rangeMapper: ComponentMapper<Range>

    override fun processSystem() {}

    fun getOneColliding(self: Int): Int? {
        val selfBB = getEntityBB(self)
        for (other in entityIds) {
            if (other == self) continue
            val otherBB = getEntityBB(other)
            if (selfBB.collides(otherBB))
                return other
        }
        return null
    }

    fun getAllColliding(self: Int): List<Int> {
        throw NotImplementedError()
    }

    fun getEntityBB(e: Int): BoundingBox {
        val position = positionMapper.getNullable(e) ?: return NullBoundingBox()
        val size = sizeMapper.getNullable(e) ?: return NullBoundingBox()
        return CircularBoundingBox(position.x, position.y, size.size / 2)
    }

    fun getRangeBB(e: Int): BoundingBox {
        val range = rangeMapper.getNullable(e)?.range ?: return NullBoundingBox()
        val position = positionMapper.getNullable(e) ?: return NullBoundingBox()
        return CircularBoundingBox(position.x, position.y, range)
    }
}

fun pointCollidesCircle(px: Float, py: Float, x: Float, y: Float, radius: Float) =
    Vector2(px, py).dst2(x, y) < radius * radius

fun circleCollidesCircle(x1: Float, y1: Float, r1: Float, x2: Float, y2: Float, r2: Float) =
    Vector2(x1, y1).dst2(x2, y2) < (r1 + r2) * (r1 + r2)
