package com.spacegame.util.voroni

import kotlin.collections.ArrayList

/**
 * Delaunay class that uses the class Delaunator to generate the delaunay triangulation. The class
 * has helpful methods for drawing the delaunay triangulation onto a path.
 * @param coordinates points x,y pair coordinates [x1,y1, x2,y2, x3,y3,...]
 */
class Delaunay(vararg var coordinates: Double) {
    constructor(coordinates: List<Double>) : this(*coordinates.toDoubleArray())

    internal var delaunator = Delaunator(coordinates)
    internal var hullIndexTemp = IntArray(coordinates.size / 2)
    internal var tempPoint: Point = Point()

    lateinit var collinear: ArrayList<Int>
    lateinit var halfEdges: IntArray
    lateinit var hull: IntArray
    lateinit var triangles: IntArray
    var inedges = IntArray(coordinates.size / 2)

    init {
        init()
    }

    internal fun init() {

        // check for collinear
        if (delaunator.hull.size > 2 && isCollinear(delaunator)) {
            collinear = (0 until coordinates.size / 2).toList() as ArrayList<Int>
            getSorterIndices(coordinates, collinear)

            val e = collinear[0]
            val f = collinear[collinear.size - 1]
            val bounds =
                arrayListOf(coordinates[2 * e], coordinates[2 * e + 1], coordinates[2 * f], coordinates[2 * f + 1])
            val r =
                (1e-8 * Math.sqrt((bounds[3] - bounds[1]) * (bounds[3] - bounds[1]) + (bounds[2] - bounds[0]) * (bounds[2] - bounds[0])))

            val n = coordinates.size / 2
            for (i in 0 until n) {
                jitter(coordinates[2 * i], coordinates[2 * i + 1], r, tempPoint)
                coordinates[2 * i] = tempPoint.x
                coordinates[2 * i + 1] = tempPoint.y
            }
            delaunator = Delaunator(coordinates)

        } else {
            collinear = ArrayList()
        }

        halfEdges = delaunator.halfEdges
        hull = delaunator.hull
        triangles = delaunator.triangles
        inedges.fill(-1)
        hullIndexTemp.fill(-1)

        // compute an index from each point to an (arbitrary) incoming halfedge used to give the first
        // neighbor of each point. For this reason, on the hull we give priority to exterior halfedges
        var n = halfEdges.size
        for (e in 0 until n) {
            val index = if (e % 3 == 2) {
                e - 2
            } else {
                e + 1
            }
            val p = triangles[index]
            if (halfEdges[e] == -1 || inedges[p] == -1) {
                inedges[p] = e
            }
        }

        n = hull.size
        for (i in 0 until n) {
            hullIndexTemp[hull[i]] = i
        }

        // degenerate case: 1 or 2 (distinct) points
        if (hull.size in 1..2) {
            triangles = intArrayOf(-1, -1, -1)
            halfEdges = intArrayOf(-1, -1, -1)

            triangles[0] = hull[0]
            if (hull.size == 2) {
                triangles[1] = hull[1]
                triangles[2] = hull[1]
            } else {
                triangles[1] = 0
                triangles[2] = 0
            }

            inedges[hull[0]] = 1
            if (hull.size == 2) {
                inedges[hull[1]] = 0
            }
        }
    }

    /**
     * Generate voronoi using the current delaunay object and giving bound as
     * RectD object.
     * @param bound bound for the voronoi
     */
    fun voronoi(bound: Rectangle? = null): Voronoi {
        return if (bound != null) {
            Voronoi(this, bound)
        } else {
            Voronoi(this)
        }
    }

    /**
     * Generate voronoi using the current delaunay object and giving bound as
     * separate objects.
     * @param left left of the bound
     * @param top top of the bound
     * @param right right of the bound
     * @param bottom bottom of the bound
     */
    fun voronoi(left: Double = 0.0, top: Double = 0.0, right: Double = 100.0, bottom: Double = 50.0): Voronoi {
        return voronoi(Rectangle(left, top, right, bottom))
    }

    fun neighbors(i: Int) = sequence {

        // degenerate case with several collinear points
        if (collinear.size > 0) {
            val l = collinear.indexOf(i)
            if (l > 0) yield(collinear[l - 1])
            if (l < collinear.size - 1) yield(collinear[l + 1])
            return@sequence
        }

        val e0 = inedges[i]
        // coincident point
        if (e0 != -1) {
            var e = e0
            var p0 = -1
            do {
                p0 = triangles[e]
                yield(p0)
                e = if (e % 3 == 2) {
                    e - 2
                } else {
                    e + 1
                }
                // bad triangulation
                if (triangles[e] != i) {
                    return@sequence
                }
                e = halfEdges[e]
                if (e == -1) {
                    val p = hull[(hullIndexTemp[i] + 1) % hull.size]
                    if (p != p0) {
                        yield(p)
                    }
                    return@sequence
                }
            } while (e != e0)
        }

    }

    internal fun find(x: Double, y: Double, i: Int = 0): Int {
        if (x.isNaN() || y.isNaN()) {
            return -1
        }
        var i = i
        val i0 = i
        var c = step(i, x, y)
        while (c >= 0 && c != i && c != i0) {
            i = c
            c = step(i, x, y)
        }
        return c
    }

    internal fun step(i: Int, x: Double, y: Double): Int {
        if (inedges[i] == -1 || coordinates.isEmpty()) {
            return (i + 1) % (coordinates.size shr 1)
        }
        var c = i
        var dc =
            (x - coordinates[i * 2]) * (x - coordinates[i * 2]) +
                    (y - coordinates[i * 2 + 1]) * (y - coordinates[i * 2 + 1])
        val e0 = inedges[i]
        var e = e0
        do {
            val t = triangles[e]
            val dt = (x - coordinates[t * 2]) * (x - coordinates[t * 2]) +
                    (y - coordinates[t * 2 + 1]) * (y - coordinates[t * 2 + 1])
            if (dt < dc) {
                dc = dt
                c = t
            }
            e = if (e % 3 == 2) {
                e - 2
            } else {
                e + 1
            }
            // bad triangulation
            if (triangles[e] != i) {
                break
            }
            e = halfEdges[e]
            if (e == -1) {
                e = hull[(hullIndexTemp[i] + 1) % hull.size]
                if (e != t) {
                    if ((x - coordinates[e * 2]) * (x - coordinates[e * 2]) +
                        (y - coordinates[e * 2 + 1]) * (y - coordinates[e * 2 + 1]) < dc
                    ) {
                        return e
                    }
                }
                break
            }
        } while (e != e0)
        return c
    }

    /**
     * Get the total number of lines, that will be generated by the delaunay triangulation,
     * that includes half-edge lines and the hull lines.
     */
    fun getLineArraySize(): Int {

        // get only the positive values
        var halfEdgeSize = halfEdges.size
        for (i in 0 until halfEdgeSize) {
            val j = halfEdges[i]
            if (j < i) {
                halfEdgeSize--
            }
        }
        return halfEdgeSize + hull.size
    }

    /**
     * Get all Line coordinates that includes half-edge lines and the hull lines as a double
     * array (4 double values per line) for the two points, as each point is made out of x, y
     * coordinates pair: [x1,y1,x2,y2,  x3,y3,x4,y4, ...]
     * @param lineArraySize number of total amount of lines, that includes half-edge lines and the hull lines
     */
    fun getLinesCoordinates(lineArraySize: Int = getLineArraySize()): DoubleArray {

        val linesPointIndices = getLinesPointIndices(lineArraySize)

        // get the coordinates for each line
        val tempCoordinates = DoubleArray(lineArraySize * 4)
        for (i in linesPointIndices.indices step 2) {
            val i1 = linesPointIndices[i]
            val i2 = linesPointIndices[i + 1]
            tempCoordinates[(i / 2) * 4] = coordinates[i1 * 2]
            tempCoordinates[(i / 2) * 4 + 1] = coordinates[i1 * 2 + 1]
            tempCoordinates[(i / 2) * 4 + 2] = coordinates[i2 * 2]
            tempCoordinates[(i / 2) * 4 + 3] = coordinates[i2 * 2 + 1]
        }

        return tempCoordinates
    }

    /**
     * Get the indices of the points that made up each line, and return them as an
     * array. Each line has two points, each one containing the x and y coordinates. But
     * since we know that if we have the index of the x coordinate, and we increase it by +1
     * we will get the index for y coordinate. This is why we only have 2 indices per line,
     * one for each point!
     * @param lineArraySize number of total amount of lines, that includes half-edge lines and the hull lines
     */
    fun getLinesPointIndices(lineArraySize: Int = getLineArraySize()): IntArray {

        var edgeCount = 0
        val indices = IntArray(lineArraySize * 2)

        // half-edge lines
        for (i in halfEdges.indices) {
            val j = halfEdges[i]
            if (j < i) {
                continue
            }
            val ti = triangles[i]
            val tj = triangles[j]

            val index = edgeCount * 2
            indices[index] = ti
            indices[index + 1] = tj
            edgeCount++
        }

        // hull lines
        for (i in 0 until hull.size - 1) {
            val h1 = hull[i]
            val h2 = hull[i + 1]

            val index = (edgeCount + i) * 2
            indices[index] = h1
            indices[index + 1] = h2
        }

        // close path
        val index = (edgeCount + hull.size - 1) * 2
        indices[index] = hull[0]
        indices[index + 1] = hull[hull.size - 1]

        return indices
    }

    /**
     * Get all delaunay triangles coordinates as a double array 6 double values per triangle,
     * for the three points, as each point has x and y coordinate pairs:
     * [x1,y1,x2,y2,x3,y3,  x4,y4,x5,y5,x6,y6 ...]
     */
    fun getTrianglesCoordinates(): DoubleArray {

        val tempCoordinates = DoubleArray(triangles.size * 2)
        for (i in 0 until triangles.size / 3) {

            // get triangle vertex indices
            val i0 = triangles[i * 3]
            val i1 = triangles[i * 3 + 1]
            val i2 = triangles[i * 3 + 2]

            // vertex 1
            tempCoordinates[i * 6 + 0] = coordinates[i0 * 2]
            tempCoordinates[i * 6 + 1] = coordinates[i0 * 2 + 1]

            // vertex 2
            tempCoordinates[i * 6 + 2] = coordinates[i1 * 2]
            tempCoordinates[i * 6 + 3] = coordinates[i1 * 2 + 1]

            // vertex 3
            tempCoordinates[i * 6 + 4] = coordinates[i2 * 2]
            tempCoordinates[i * 6 + 5] = coordinates[i2 * 2 + 1]
        }
        return tempCoordinates
    }

    /**
     * Get the indices of the points that made up each triangle, and return them as an
     * array. Each triangle has three points, each one containing the x and y coordinates. But
     * since we know that if we have the index of the x coordinate, and we increase it by +1
     * we will get the index for y coordinate. This is why we only have 3 indices per triangle,
     * one for each point!
     */
    fun getTrianglesPointIndices(): IntArray {
        return triangles.clone()
    }

    /**
     * Get all center coordinates for each delaunay triangle as a double array 2 double values
     * per center point, as each point has x and y coordinates pairs. Array is in format: [x1,y1,  x2,y2, ...]
     *
     * Each center coordinates matches the index of the triangle, for example the center point
     * coordinates x and y on index 1:
     * val x = getTriangleCenterCoordinates()[1*2]
     * val y = getTriangleCenterCoordinates()[1*2 + 1]
     *
     * matches the triangle on the same index (triangle with coordinates x1,y1, x2,y2 and x3,y3)
     * val x1 = getTrianglesCoordinates()[1*6]
     * val y1 = getTrianglesCoordinates()[1*6 + 1]
     * val x2 = getTrianglesCoordinates()[1*6 + 2]
     * val y2 = getTrianglesCoordinates()[1*6 + 3]
     * val x3 = getTrianglesCoordinates()[1*6 + 4]
     * val y3 = getTrianglesCoordinates()[1*6 + 5]
     */
    fun getTriangleCenterCoordinates(): DoubleArray {

        val newCoordinates = DoubleArray((triangles.size / 3) * 2)
        for (i in 0 until triangles.size / 3) {

            // get the middle point of the triangle
            getTriangleCenter(i, tempPoint)
            newCoordinates[i * 2] = tempPoint.x
            newCoordinates[i * 2 + 1] = tempPoint.y
        }
        return newCoordinates
    }

    /**
     * Get specific triangle center by given index from delaunay triangulation
     * @param i triangle index
     * @param point existing point, that way there is no need to create new point each time
     */
    fun getTriangleCenter(i: Int, point: Point = Point()): Point {

        val j = i * 3
        val i0 = triangles[j]
        val i1 = triangles[j + 1]
        val i2 = triangles[j + 2]

        point.x = (coordinates[i0 * 2] + coordinates[i1 * 2] + coordinates[i2 * 2]) / 3
        point.y = (coordinates[i0 * 2 + 1] + coordinates[i1 * 2 + 1] + coordinates[i2 * 2 + 1]) / 3
        return point
    }

    /**
     * Update the delaunator after change to the coordinates is made to update
     * all other values.
     */
    fun update(): Delaunay {

        // update delaunator
        delaunator.coords = coordinates
        delaunator.update()

        init()
        return this
    }

    companion object {

        /**
         * Generate Delaunay using array list of Point points, that hold the
         * x and y coordinates
         * @param points array list with the points for the delaunay
         */
        fun from(points: ArrayList<Point>): Delaunay {
            val coordinates = DoubleArray(points.size * 2)
            for (i in points.indices) {
                coordinates[i * 2] = points[i].x
                coordinates[i * 2 + 1] = points[i].y
            }
            return Delaunay(*coordinates)
        }

        /**
         * Generate Delaunay using array of Point points, that hold the
         * x and y coordinates
         * @param points array with the points for the delaunay
         */
        fun from(vararg points: Point): Delaunay {
            val coordinates = DoubleArray(points.size * 2)
            for (i in points.indices) {
                coordinates[i * 2] = points[i].x
                coordinates[i * 2 + 1] = points[i].y
            }
            return Delaunay(*coordinates)
        }

        /**
         * Check if triangulation is collinear when all its triangles have a non-null area
         * @param delaunator delaunator with the coordinates and triangles indices
         */
        internal fun isCollinear(delaunator: Delaunator): Boolean {

            val triangles = delaunator.triangles
            val coordinates = delaunator.coords
            for (i in triangles.indices step 3) {
                val a = 2 * triangles[i]
                val b = 2 * triangles[i + 1]
                val c = 2 * triangles[i + 2]
                val cross =
                    (coordinates[c] - coordinates[a]) * (coordinates[b + 1] - coordinates[a + 1]) - (coordinates[b] - coordinates[a]) * (coordinates[c + 1] - coordinates[a + 1])
                if (cross > 1e-10) {
                    return false
                }
            }
            return true
        }

        internal fun jitter(x: Double, y: Double, r: Double, p: Point) {
            p.x = x + Math.sin(x + y) * r
            p.y = y + Math.cos(x - y) * r
        }

        /**
         * Sort array list with indices using there corresponding points coordinates from
         * smaller to bigger.
         * @param coordinates double array with the x,y coordinates pair [x1,y1, x2,y2, x3,y3...]
         * @param indices array list with indices for each point pair [0,1,2,...]
         */
        internal fun getSorterIndices(coordinates: DoubleArray, indices: ArrayList<Int>) {

            val temp = coordinates.copyOf(coordinates.size)
            for (x in coordinates.indices step 2) {
                for (y in x + 2 until coordinates.size - 1 step 2) {
                    if (temp[y] < temp[x] || (temp[y] == temp[x] && temp[y + 1] < temp[x + 1])) {
                        temp.swap(y, x)
                        temp.swap(y + 1, x + 1)

                        indices.swap(x / 2, y / 2)
                    }
                }
            }
        }

        /**
         * Extension function to the array list for swapping elements at two given indices.
         * This is generic function for all types.
         * @param index1 first index
         * @param index2 second index
         */
        internal fun <T> ArrayList<T>.swap(index1: Int, index2: Int) {
            val tmp = this[index1] // 'this' corresponds to the list
            this[index1] = this[index2]
            this[index2] = tmp
        }

        /**
         * Extension function to the DoubleArray for swapping elements at two given indices.
         * @param index1 first index
         * @param index2 second index
         */
        internal fun DoubleArray.swap(index1: Int, index2: Int) {
            val tmp = this[index1]
            this[index1] = this[index2]
            this[index2] = tmp
        }
    }
}