package com.spacegame.util.voroni

/**
 * Class for generating svg path data string, from simple methods. Used to generate svg path data
 * from the delaunay triangulation or the voronoi diagram. To learn more about SVG paths visit:
 * https://www.w3.org/TR/SVG/paths.html
 * @param data initial path data string
 */
data class Path(
    var data: String = "",
    var bound: Rectangle = Rectangle(
        Double.POSITIVE_INFINITY,
        Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY,
        Double.NEGATIVE_INFINITY
    )
) {

    companion object {
        const val epsilon = 1e-6
    }

    var x0: Double? = null
    var y0: Double? = null
    var x1: Double? = null
    var y1: Double? = null

    /**
     * Add instruction to move the point location to a new position
     * @param x x coordinate of the new position
     * @param y y coordinate of the new position
     */
    fun moveTo(x: Double, y: Double) {

        // add moveTo command
        x0 = x
        y0 = y
        x1 = x
        y1 = y
        data += "M${x},${y}"

        // update bound
        setBound(x, y)
    }

    /**
     * Add instruction to generate new line from previous point to a new point
     * @param x x coordinate of the new point
     * @param y y coordinate of the new point
     */
    fun lineTo(x: Double, y: Double) {

        // add lineTo command
        x1 = x
        y1 = y
        data += "L${x},${y}"

        // update bound
        setBound(x, y)
    }

    /**
     * Add instruction for closing the path, to the svg path data string
     */
    fun closePath() {
        if (x1 != null && y1 != null) {
            x1 = x0
            y1 = y0
            data += "Z"
        }
    }

    /**
     * Add instruction to generate arc, to the svg path data string
     * @param x center x coordinate of the arc
     * @param y center y coordinate of the arc
     * @param r radius of the arc
     * @param isLineToCalled if before drawing the arc, lineTo() or moveTo() command is applied
     */
    fun arc(x: Double, y: Double, r: Double, isLineToCalled: Boolean = false) {
        val x0 = x + r
        val y0 = y

        if (!isLineToCalled || (x1 == null && y1 == null)) {
            data += "M${x0},${y0}"
        } else if (x1 != null && y1 != null && Math.abs(x1!! - x0) > epsilon || Math.abs(y1!! - y0) > epsilon) {
            data += "L${x0},${y0}"
        }

        if (r == 0.0) {
            return
        }

        x1 = x0
        y1 = y0
        data += "A${r},${r},0,1,1,${x - r},${y}A${r},${r},0,1,1,${x0},${y0}"

        // update bound
        setBound(x - r, y - r)
        setBound(x + r, y + r)
    }

    /**
     * Add instruction to generate rectangle, to the svg path data string
     * @param x x coordinate of the rectangle, which is in the top-left corner
     * @param y y coordinate of the rectangle, which is in the top-left corner
     * @param w width of the rectangle
     * @param h height of the rectangle
     */
    fun rect(x: Double, y: Double, w: Double, h: Double) {
        x0 = x
        x1 = x
        y0 = y
        y1 = y
        data += "M${x},${y}h${+w}v${+h}h${-w}Z"

        // update bound
        setBound(x, y)
        setBound(x + w, y + h)
    }

    internal fun setBound(x: Double, y: Double) {

        if (x > bound.right) {
            bound.right = x
        }

        if (x < bound.left) {
            bound.left = x
        }

        if (y > bound.top) {
            bound.top = y
        }

        if (y < bound.bottom) {
            bound.bottom = y
        }
    }

    override fun toString(): String {
        return data
    }
}