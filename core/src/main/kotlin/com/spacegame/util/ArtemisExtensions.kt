package com.spacegame.util

import com.artemis.Aspect
import com.artemis.BaseSystem
import com.artemis.Component
import com.artemis.ComponentMapper
import com.artemis.utils.IntBag

fun <T : Component> ComponentMapper<T>.getNullable(e: Int): T? {
    if (!has(e))
        return null
    return get(e)
}

fun <T : Component> ComponentMapper<T>.toggle(e: Int): T? =
    set(e, !has(e))

fun <T : Component> ComponentMapper<T>.addConnection(e: Int): T? =
    set(e, true)

fun IntBag.toList(): List<Int> = data.toList().subList(0, size())

operator fun IntBag.iterator(): Iterator<Int> = toList().iterator()