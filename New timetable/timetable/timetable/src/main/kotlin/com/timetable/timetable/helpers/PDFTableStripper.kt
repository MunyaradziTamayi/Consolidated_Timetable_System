package com.timetable.timetable.helpers

import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.font.PDType3Font
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.PDFTextStripperByArea
import org.apache.pdfbox.text.TextPosition
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.*


open class PDFTableStripper : PDFTextStripper() {
    private var flipAT: AffineTransform? = null
    private var rotateAT: AffineTransform? = null


    protected var boxes: MutableSet<Rectangle2D>? = null

    // Border to allow when finding intersections
    private val dx = 1.0 // This value works for me, feel free to tweak (or add setter)
    private val dy = 0.000 // Rows of text tend to overlap, so need to extend

    /**
     * Region in which to find table (otherwise whole page)
     */
    private var regionArea: Rectangle2D? = null

    /**
     * Number of rows in inferred table
     */
    var rows: Int = 0
        private set

    /**
     * Number of columns in inferred table
     */
    var columns: Int = 0
        private set

    /**
     * This is the object that does the text extraction
     */
    private val regionStripper: PDFTextStripperByArea

    /**
     * 1D intervals - used for calculateTableRegions()
     * @author Beldaz
     */
    class Interval(var start: Double, var end: Double) {
        fun add(col: Interval) {
            if (col.start < start) start = col.start
            if (col.end > end) end = col.end
        }

        companion object {
            fun addTo(x: Interval, columns: LinkedList<Interval>) {
                var p = 0
                val it = columns.iterator()
                // Find where x should go
                while (it.hasNext()) {
                    val col = it.next()
                    if (x.end >= col.start) {
                        if (x.start <= col.end) { // overlaps
                            x.add(col)
                            it.remove()
                        }
                        break
                    }
                    ++p
                }
                while (it.hasNext()) {
                    val col = it.next()
                    if (x.start > col.end) break
                    x.add(col)
                    it.remove()
                }
                columns.add(p, x)
            }
        }
    }


    init {
        super.setShouldSeparateByBeads(true)
        regionStripper = PDFTextStripperByArea()
        regionStripper.sortByPosition = true
    }

    /**
     * Define the region to group text by.
     *
     * @param rect The rectangle area to retrieve the text from.
     */
    fun setRegion(rect: Rectangle2D?) {
        regionArea = rect
    }

    /**
     * Get the text for the region, this should be called after extractTable().
     *
     * @return The text that was identified in that region.
     */
    fun getText(row: Int, col: Int): String {
        return regionStripper.getTextForRegion("el" + col + "x" + row)
    }

    @Throws(IOException::class)
    fun extractTable(pdPage: PDPage) {
        startPage = currentPageNo
        endPage = currentPageNo

        boxes = HashSet()
        // flip y-axis
        flipAT = AffineTransform()
        flipAT!!.translate(0.0, pdPage.bBox.height.toDouble())
        flipAT!!.scale(1.0, -1.0)

        // page may be rotated
        rotateAT = AffineTransform()
        val rotation = pdPage.rotation
        if (rotation != 0) {
            val mediaBox = pdPage.mediaBox
            when (rotation) {
                90 -> rotateAT!!.translate(mediaBox.height.toDouble(), 0.0)
                270 -> rotateAT!!.translate(0.0, mediaBox.width.toDouble())
                180 -> rotateAT!!.translate(mediaBox.width.toDouble(), mediaBox.height.toDouble())
                else -> {}
            }
            rotateAT!!.rotate(Math.toRadians(rotation.toDouble()))
        }
        OutputStreamWriter(ByteArrayOutputStream()).use { dummy ->
            super.output = dummy
            super.processPage(pdPage)
        }
        val regions = calculateTableRegions()

        //        System.err.println("Drawing " + nCols + "x" + nRows + "="+ nRows*nCols + " regions");
        for (i in 0..<columns) {
            for (j in 0..<rows) {
                val region = regions[i][j]
                regionStripper.addRegion("el" + i + "x" + j, region)
            }
        }

        regionStripper.extractRegions(pdPage)
    }

    /**
     * Infer a rectangular grid of regions from the boxes field.
     *
     * @return 2D array of table regions (as Rectangle2D objects). Note that
     * some of these regions may have no content.
     */
    private fun calculateTableRegions(): Array<Array<Rectangle2D?>> {
        // Build up a list of all table regions, based upon the populated
        // regions of boxes field. Treats the horizontal and vertical extents
        // of each box as distinct

        val columns = LinkedList<Interval>()
        val rows = LinkedList<Interval>()

        for (box in boxes!!) {
            val x = Interval(box.minX, box.maxX)
            val y = Interval(box.minY, box.maxY)

            Interval.addTo(x, columns)
            Interval.addTo(y, rows)
        }

        this.rows = rows.size
        this.columns = columns.size
        val regions = Array(
            this.columns
        ) { arrayOfNulls<Rectangle2D>(this.rows) }
        var i = 0
        // Label regions from top left, rather than the transformed orientation
        for (column in columns) {
            var j = 0
            for (row in rows) {
                regions[this.columns - i - 1][this.rows - j - 1] =
                    Rectangle2D.Double(column.start, row.start, column.end - column.start, row.end - row.start)
                ++j
            }
            ++i
        }

        return regions
    }

    private fun findMinimumX(): Double {
        var min = 10000.0
        for (box in boxes!!) {
            if (box.minX < min) {
                min = box.minX
            }
        }

        return min
    }

    private val firstColumnBoxes: ArrayList<Rectangle2D>
        get() {
            val min = findMinimumX()
            val firstColumn =
                ArrayList<Rectangle2D>()
            for (box in boxes!!) {
                if (box.minX == min) {
                    firstColumn.add(box)
                }
            }

            return firstColumn
        }

    private fun findMinimumY(): Double {
        val firstColumn = firstColumnBoxes
        var min = 100000.0
        for (box in firstColumn) {
            if (box.minY < min) {
                min = box.minY
            }
        }

        return min
    }

    private fun findMaximumY(): Double {
        val firstColumn = firstColumnBoxes
        var max = 0.0
        for (box in firstColumn) {
            if (box.maxY > max) {
                max = box.maxY
            }
        }

        return max
    }

    private fun removeJunk() {
        val minY = findMinimumY()
        val maxY = findMaximumY()

        boxes!!.removeIf { box: Rectangle2D -> box.minY < minY || box.maxY > maxY }
    }

    /**
     * Register each character's bounding box, updating boxes field to maintain
     * a list of all distinct groups of characters.
     *
     * Overrides the default functionality of PDFTextStripper.
     * Most of this is taken from DrawPrintTextLocations.java, with extra steps
     * at end of main loop
     */
    @Throws(IOException::class)
    override fun writeString(string: String, textPositions: List<TextPosition>) {
        for (text in textPositions) {
            // glyph space -> user space
            // note: text.getTextMatrix() is *not* the Text Matrix, it's the Text Rendering Matrix
            val at = text.textMatrix.createAffineTransform()
            val font = text.font
            val bbox = font.boundingBox

            // advance width, bbox height (glyph space)
            val xadvance = font.getWidth(text.characterCodes[0]) // todo: should iterate all chars
            val rect = Rectangle2D.Float(0f, bbox.lowerLeftY, xadvance, bbox.height)

            if (font is PDType3Font) {
                // bbox and font matrix are unscaled
                at.concatenate(font.getFontMatrix().createAffineTransform())
            } else {
                // bbox and font matrix are already scaled to 1000
                at.scale((1 / 1000f).toDouble(), (1 / 1000f).toDouble())
            }
            var s = at.createTransformedShape(rect)
            s = flipAT!!.createTransformedShape(s)
            s = rotateAT!!.createTransformedShape(s)


            //
            // Merge character's bounding box with boxes field
            //
            val bounds = s.bounds2D
            // Pad sides to detect almost touching boxes
            val hitbox = bounds.bounds2D
            hitbox.add(bounds.minX - dx, bounds.minY - dy)
            hitbox.add(bounds.maxX + dx, bounds.maxY + dy)

            // Find all overlapping boxes
            val intersectList: MutableList<Rectangle2D> = ArrayList()
            for (box in boxes!!) {
                if (box.intersects(hitbox)) {
                    intersectList.add(box)
                }
            }

            // Combine all touching boxes and update
            // (NOTE: Potentially this could leave some overlapping boxes un-merged,
            // but it's sufficient for now and get's fixed up in calculateTableRegions)
            for (box in intersectList) {
                bounds.add(box)
                boxes!!.remove(box)
            }
            boxes!!.add(bounds)
        }
    }

    /**
     * This method does nothing in this derived class, because beads and regions are incompatible. Beads are
     * ignored when stripping by area.
     *
     * @param aShouldSeparateByBeads The new grouping of beads.
     */
    override fun setShouldSeparateByBeads(aShouldSeparateByBeads: Boolean) {
    }

    /**
     * Adapted from PDFTextStripperByArea
     * {@inheritDoc}
     */
    override fun processTextPosition(text: TextPosition) {
        if (regionArea != null && !regionArea!!.contains(text.x.toDouble(), text.y.toDouble())) {
            // skip character
        } else {
            super.processTextPosition(text)
        }
    }
}