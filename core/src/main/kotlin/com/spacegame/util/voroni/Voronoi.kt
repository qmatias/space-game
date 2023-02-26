package com.spacegame.util.voroni

import com.slaviboy.voronoi.Polygon.Companion.center

/**
 * Class for computing the Voronoi diagram of a set of two-dimensional points. It is based on Delaunator,
 * a fast library for computing the Delaunay triangulation using sweep algorithms. The Voronoi diagram is
 * constructed by connecting the circumcenters of adjacent triangles in the Delaunay triangulation.
 * @param delaunay delaunay object holding information about the delaunay triangulation
 * @param bound boundary box coordinate that determines where the clipping will happen
 */
class Voronoi(var delaunay: Delaunay, var bound: Rectangle = Rectangle(0.0, 0.0, 960.0, 500.0)) {

    lateinit var vectors: DoubleArray
    lateinit var circumcenters: DoubleArray              // array with the circumcenters of the circle around the delaunay triangle
    internal lateinit var tempPoint: Point    // temp point object that is reused and holds the centroid of each polygon(cell)

    init {
        if (bound.right.isNaN() || bound.top.isNaN() || bound.right < bound.left || bound.top < bound.bottom) {
            throw IllegalArgumentException("Invalid bounds!")
        }
        init()
    }

    internal fun init() {

        tempPoint = Point()

        val coordinates = delaunay.coordinates
        val hull = delaunay.hull
        val triangles = delaunay.triangles

        // compute circumcenters
        circumcenters = DoubleArray((triangles.size / 3) * 2)
        var j = 0
        var x: Double
        var y: Double
        for (i in 0 until triangles.size / 3) {

            val i1 = triangles[i * 3] * 2
            val i2 = triangles[i * 3 + 1] * 2
            val i3 = triangles[i * 3 + 2] * 2

            val x1 = coordinates[i1]
            val y1 = coordinates[i1 + 1]

            val x2 = coordinates[i2]
            val y2 = coordinates[i2 + 1]

            val x3 = coordinates[i3]
            val y3 = coordinates[i3 + 1]

            val dx = x2 - x1
            val dy = y2 - y1
            val ex = x3 - x1
            val ey = y3 - y1
            val bl = dx * dx + dy * dy
            val cl = ex * ex + ey * ey
            val ab = (dx * ey - dy * ex) * 2

            if (ab.isNaN() || ab == 0.0) {
                // degenerate case (collinear diagram)
                x = ((x1 + x3) / 2.0 - 1e8 * ey)
                y = ((y1 + y3) / 2.0 + 1e8 * ex)
            } else if (Math.abs(ab) < 1e-8) {
                // almost equal points (degenerate triangle)
                x = (x1 + x3) / 2.0
                y = (y1 + y3) / 2.0
            } else {
                val d = 1.0 / ab
                x = x1 + (ey * bl - dy * cl) * d
                y = y1 + (dx * cl - ex * bl) * d
            }
            circumcenters[j * 2] = x
            circumcenters[j * 2 + 1] = y

            j++
        }

        vectors = DoubleArray(delaunay.coordinates.size * 2)

        // Compute exterior cell rays.
        var h = hull[hull.size - 1]
        var p0: Double
        var p1 = h * 4.0
        var x0: Double
        var x1 = coordinates[2 * h]
        var y0: Double
        var y1 = coordinates[2 * h + 1]
        for (i in hull.indices) {
            h = hull[i]
            p0 = p1
            x0 = x1
            y0 = y1
            p1 = h * 4.0
            x1 = coordinates[2 * h]
            y1 = coordinates[2 * h + 1]
            vectors[(p0 + 2).toInt()] = y0 - y1
            vectors[(p1).toInt()] = y0 - y1
            vectors[(p0 + 3).toInt()] = x1 - x0
            vectors[(p1 + 1).toInt()] = x1 - x0
        }
    }

    /**
     * Update the delaunay after change to the coordinates is made to update
     * all other values.
     */
    fun update(): Voronoi {
        delaunay.update()
        init()
        return this
    }

    /**
     * Get the center point as (x,y coordinates pairs) for each polygon(cell) as array list, two double values
     * per center point, as each point has x and y coordinates pairs. Array is in format: [x1,y1,  x2,y2, ...]
     */
    fun getCellsCenterCoordinates(): List<Point> {
        return getCells(false).map { center(it) }
    }

    /**
     * Get all coordinates for each polygon(cell) as array list. Each point of the polygon has 2 coordinates
     * x and y that means array list is in format: [ [x1,y1,  x2,y2, ...], [x1,y1,  x2,y2, ...], ... ].
     * If path is closed that means the coordinates from the first point are added to the end of the array list.
     * @param isPathClosed whether the path should be closed and the first point coordinates should be added at the end of the array list
     */
    fun getCells(isPathClosed: Boolean = true): ArrayList<ArrayList<Double>> {

        val coordinatesList = ArrayList<ArrayList<Double>>()
        for (i in 0 until delaunay.coordinates.size / 2) {
            val coordinate = clip(i) ?: ArrayList()
            coordinatesList.add(coordinate)
        }

        // if path is closed att the first coordinates of each polygon to the end of there corresponding array list
        if (isPathClosed) {
            for (i in 0 until delaunay.coordinates.size / 2) {
                coordinatesList[i].add(coordinatesList[i][0])
                coordinatesList[i].add(coordinatesList[i][1])
            }
        }
        return coordinatesList
    }


    /**
     * Apply Lloyd relaxation for all cells in the current voronoi diagram. The algorithm computes the Voronoi
     * diagram for a set of points, moves each point towards the centroid of its Voronoi region, and repeats for
     * the number of iterations given by the user.
     * @param iterations how many time to do the relaxation algorithm
     */
    fun relax(iterations: Int) {

        // loop for the iterations
        for (t in 0 until iterations) {

            // update coordinates and apply update after each iterations
            for (i in 0 until delaunay.coordinates.size / 2) {

                val coordinate = clip(i) ?: ArrayList()
                center(coordinate, tempPoint)
                delaunay.coordinates[i * 2] = tempPoint.x
                delaunay.coordinates[i * 2 + 1] = tempPoint.y
                update()
            }
        }
    }

    fun neighbors(i: Int) = sequence {
        val ci = clip(i)
        if (ci != null)
            for (j in delaunay.neighbors(i)) {
                val cj = clip(j)
                // find the common edge
                if (cj != null)
                    loop@ for (ai in 0 until ci.size step 2) {
                        val li = ci.size
                        for (aj in 0 until cj.size step 2) {
                            val lj = cj.size
                            if (ci[ai] == cj[aj]
                                && ci[ai + 1] == cj[aj + 1]
                                && ci[(ai + 2) % li] == cj[(aj + lj - 2) % lj]
                                && ci[(ai + 3) % li] == cj[(aj + lj - 1) % lj]
                            ) {
                                yield(j)
                                break@loop
                            }
                        }
                    }
            }
    }

    /**
     * Get the polygon cell points as raw data, without
     * applying clipping.
     */
    internal fun rawCell(i: Int): ArrayList<Double>? {
        val inedges = delaunay.inedges
        val halfedges = delaunay.halfEdges
        val triangles = delaunay.triangles
        val e0 = inedges[i]
        if (e0 == -1) return null // coincident point

        val points = ArrayList<Double>()
        var e = e0
        do {
            val t = (Math.floor(e / 3.0)).toInt()
            points.add(circumcenters[t * 2])
            points.add(circumcenters[t * 2 + 1])
            e = if (e % 3 == 2) {
                e - 2
            } else {
                e + 1
            }
            if (triangles[e] != i) break // bad triangulation
            e = halfedges[e]
        } while (e != e0 && e != -1)
        return points
    }

    internal fun clip(i: Int): ArrayList<Double>? {

        // degenerate case (1 valid point: return the box)
        if (i == 0 && delaunay.hull.size == 1) {
            return arrayListOf(
                this.bound.right,
                this.bound.bottom,
                this.bound.right,
                this.bound.top,
                this.bound.left,
                this.bound.top,
                this.bound.left,
                this.bound.bottom
            )
        }
        val points = rawCell(i) ?: return null

        val v = i * 4
        return if (vectors[v] != 0.0 || vectors[v + 1] != 0.0) {
            clipInfinite(i, points, vectors[v], vectors[v + 1], vectors[v + 2], vectors[v + 3])
        } else {
            clipFinite(i, points)
        }
    }

    internal fun clipInfinite(
        i: Int,
        points: ArrayList<Double>,
        vx0: Double,
        vy0: Double,
        vxn: Double,
        vyn: Double
    ): ArrayList<Double>? {
        var pts = ArrayList<Double>(points)
        var p: Point? = null

        p = project(pts[0], pts[1], vx0, vy0)
        if (p != null) {
            pts.add(0, p.x)
            pts.add(1, p.y)
        }

        p = project(pts[pts.size - 2], pts[pts.size - 1], vxn, vyn)
        if (p != null) {
            pts.add(p.x)
            pts.add(p.y)
        }

        val ptsTemp = clipFinite(i, pts)

        return when {
            ptsTemp != null -> {
                pts = ptsTemp

                var n = pts.size
                if (n < 2) {
                    return pts
                }

                var c0 = 0
                var c1 = edgeCode(pts[n - 2], pts[n - 1])

                var j = 0
                while (j < n) {
                    c0 = c1
                    c1 = edgeCode(pts[j], pts[j + 1])
                    if (c0 != 0 && c1 != 0) {
                        j = edge(i, c0, c1, pts, j)
                        n = pts.size
                    }
                    j += 2
                }
                pts
            }

            contains(i, (bound.left + bound.right) / 2, (bound.bottom + bound.top) / 2) -> {
                arrayListOf(
                    bound.left,
                    bound.bottom,
                    bound.right,
                    bound.bottom,
                    bound.right,
                    bound.top,
                    bound.left,
                    bound.top
                )
            }

            else -> return ptsTemp
        }
    }

    internal fun clipFinite(i: Int, points: ArrayList<Double>): ArrayList<Double>? {
        val n = points.size
        var pts: ArrayList<Double>? = null
        var x0 = 0.0
        var y0 = 0.0
        var x1 = points[n - 2]
        var y1 = points[n - 1]
        var c0 = 0
        var c1 = regionCode(x1, y1)
        var e0 = 0
        var e1 = 0
        for (j in 0 until points.size step 2) {
            x0 = x1
            y0 = y1
            x1 = points[j]
            y1 = points[j + 1]
            c0 = c1
            c1 = regionCode(x1, y1)
            if (c0 == 0 && c1 == 0) {
                e0 = e1
                e1 = 0
                if (pts != null) {
                    pts.add(x1)
                    pts.add(y1)
                } else {
                    pts = arrayListOf(x1, y1)
                }
            } else {
                var s: DoubleArray?
                var sx0 = 0.0
                var sy0 = 0.0
                var sx1 = 0.0
                var sy1 = 0.0
                if (c0 == 0) {
                    s = clipSegment(x0, y0, x1, y1, c0, c1)
                    if (s == null) continue
                    sx0 = s[0]
                    sy0 = s[1]
                    sx1 = s[2]
                    sy1 = s[3]
                } else {
                    s = clipSegment(x1, y1, x0, y0, c1, c0)
                    if (s == null) continue
                    sx1 = s[0]
                    sy1 = s[1]
                    sx0 = s[2]
                    sy0 = s[3]
                    e0 = e1
                    e1 = edgeCode(sx0, sy0)
                    if (e0 != 0 && e1 != 0) {
                        if (pts != null) {
                            edge(i, e0, e1, pts, pts.size)
                        }
                    }
                    if (pts != null) {
                        pts.add(sx0)
                        pts.add(sy0)
                    } else {
                        pts = arrayListOf(sx0, sy0)
                    }
                }
                e0 = e1
                e1 = edgeCode(sx1, sy1)
                if (e0 != 0 && e1 != 0) {
                    if (pts != null) {
                        edge(i, e0, e1, pts, pts.size)
                    }
                }
                if (pts != null) {
                    pts.add(sx1)
                    pts.add(sy1)
                } else {
                    pts = arrayListOf(sx1, sy1)
                }
            }
        }
        if (pts != null) {
            e0 = e1
            e1 = edgeCode(pts[0], pts[1])
            if (e0 != 0 && e1 != 0) edge(i, e0, e1, pts, pts.size)
        } else if (contains(i, (bound.left + bound.right) / 2, (bound.bottom + bound.top) / 2)) {
            return arrayListOf(
                bound.right,
                bound.bottom,
                bound.right,
                bound.top,
                bound.left,
                bound.top,
                bound.left,
                bound.bottom
            )
        }
        return pts
    }

    internal fun contains(i: Int, x: Double, y: Double): Boolean {
        if (x.isNaN() || y.isNaN()) return false
        return delaunay.step(i, x, y) == i
    }

    internal fun edgeCode(x: Double, y: Double): Int {
        val b1 = if (x == bound.left) {
            0b0001
        } else {
            if (x == bound.right) {
                0b0010
            } else {
                0b0000
            }
        }

        val b2 = if (y == bound.bottom) {
            0b0100
        } else {
            if (y == bound.top) {
                0b1000
            } else {
                0b0000
            }
        }
        return b1 or b2
    }

    internal fun edge(_i: Int, _e0: Int, e1: Int, p: ArrayList<Double>, _j: Int): Int {
        var j = _j
        var i = _i
        var e0 = _e0
        loop@ while (e0 != e1) {
            var x = 0.0
            var y = 0.0
            when (e0) {
                0b0101 -> {
                    // top-left
                    e0 = 0b0100
                    continue@loop
                }

                0b0100 -> {
                    // top
                    e0 = 0b0110
                    x = bound.right
                    y = bound.bottom
                }

                0b0110 -> {
                    // top-right
                    e0 = 0b0010
                    continue@loop
                }

                0b0010 -> {
                    // right
                    e0 = 0b1010
                    x = bound.right
                    y = bound.top
                }

                0b1010 -> {
                    // bottom-right
                    e0 = 0b1000
                    continue@loop
                }

                0b1000 -> {
                    // bottom
                    e0 = 0b1001
                    x = bound.left
                    y = bound.top
                }

                0b1001 -> {
                    // bottom-left
                    e0 = 0b0001
                    continue@loop
                }

                0b0001 -> {
                    // left
                    e0 = 0b0101
                    x = bound.left
                    y = bound.bottom
                }
            }
            if ((p.size <= _j * 2 || p[j] != x || p[j + 1] != y) && contains(i, x, y)) {
                p.add(j, x)
                p.add(j + 1, y)
                j += 2
            }
        }

        if (p.size > 4) {
            var i = 0
            while (i < p.size) {
                val j = (i + 2) % p.size
                val k = (i + 4) % p.size

                if ((p[i] == p[j] && p[j] == p[k]) || (p[i + 1] == p[j + 1] && p[j + 1] == p[k + 1])) {
                    for (t in j until (j + 2)) {
                        p.removeAt(j)
                    }
                    i -= 2
                }
                i += 2
            }
        }
        return j
    }

    internal fun project(x0: Double, y0: Double, vx: Double, vy: Double): Point? {
        var t = Double.POSITIVE_INFINITY
        var c = 0.0
        var x = 0.0
        var y = 0.0
        if (vy < 0) {
            // top
            if (y0 <= bound.bottom) return null
            c = (bound.bottom - y0) / vy
            if (c < t) {
                y = bound.bottom
                t = c
                x = x0 + t * vx
            }
        } else if (vy > 0) {
            // bottom
            if (y0 >= bound.top) return null
            c = (bound.top - y0) / vy
            if (c < t) {
                y = bound.top
                t = c
                x = x0 + t * vx
            }
        }
        if (vx > 0) {
            // right
            if (x0 >= bound.right) return null
            c = (bound.right - x0) / vx
            if (c < t) {
                x = bound.right
                t = c
                y = y0 + t * vy
            }
        } else if (vx < 0) {
            // left
            if (x0 <= bound.left) return null
            c = (bound.left - x0) / vx
            if (c < t) {
                x = bound.left
                t = c
                y = y0 + t * vy
            }
        }
        return Point(x, y)
    }

    internal fun regionCode(x: Double, y: Double): Int {
        val b1 = if (x < bound.left) {
            0b0001
        } else {
            if (x > bound.right) 0b0010
            else 0b0000
        }

        val b2 = if (y < bound.bottom) {
            0b0100
        } else {
            if (y > bound.top) 0b1000
            else 0b0000
        }
        return b1 or b2
    }

    internal fun clipSegment(x0: Double, _y0: Double, _x1: Double, _y1: Double, _c0: Int, _c1: Int): DoubleArray? {

        var x0 = x0
        var y0 = _y0
        var x1 = _x1
        var y1 = _y1
        var c0 = _c0
        var c1 = _c1
        while (true) {
            if (c0 == 0 && c1 == 0) {
                return doubleArrayOf(x0, y0, x1, y1)
            }

            if ((c0 and c1) != 0) return null
            var x: Double
            var y: Double
            val c = if (c0 != 0) c0 else c1

            if ((c and 0b1000) != 0) {
                x = x0 + (x1 - x0) * (bound.top - y0) / (y1 - y0)
                y = bound.top
            } else if ((c and 0b0100) != 0) {
                x = x0 + (x1 - x0) * (bound.bottom - y0) / (y1 - y0)
                y = bound.bottom
            } else if ((c and 0b0010) != 0) {
                y = y0 + (y1 - y0) * (bound.right - x0) / (x1 - x0)
                x = bound.right
            } else {
                y = y0 + (y1 - y0) * (bound.left - x0) / (x1 - x0)
                x = bound.left
            }

            if (c0 != 0) {
                x0 = x
                y0 = y
                c0 = regionCode(x0, y0)
            } else {
                x1 = x
                y1 = y
                c1 = regionCode(x1, y1)
            }
        }
    }

    /**
     * Holding the polygon(cell) coordinate for each points x,y pair and the index
     * @param coordinates array list with all the points coordinates from the polygon
     * @param index index of the polygon
     */
    data class CellValues(var coordinates: ArrayList<Double>, var index: Int = -1)
}