package com.spacegame.components

import com.artemis.Component

class Construction(
    var progress: Float = 0f, // 0..1
    var speed: Float = .1f // progress/second
) : Component()
