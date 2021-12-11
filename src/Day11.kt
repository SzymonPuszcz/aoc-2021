fun main() {
    val matrix = readInput("Day11")
        .map { row -> row.map { it.toString().toInt() }.toMutableList() }
        .toMutableList()
        .let { DumboMatrix(it) }

    matrix.iterate(100)
    println(matrix.flashes)
    println(matrix.countSynchronized())

}

private class DumboMatrix(
    private val data: MutableList<MutableList<Int>>
) {
    var flashes = 0L

    var iterations = 0L

    private val xSize = data.first().size

    private val ySize = data.size

    fun iterate(count: Int) {
        repeat(count) {
            performIteration()
            iterations++
        }
    }

    fun countSynchronized(): Long {
        while (!allSynchronized()) {
            iterate(1)
        }
        return iterations
    }

    private fun allSynchronized(): Boolean {
        data.forEachIndexed { y, row ->
            if (row.any { it != 0 })
                return false
        }
        return true
    }

    private fun getAdjacentCoords(x: Int, y: Int): List<Pair<Int, Int>> {
        return buildList {
            if (y != 0)
                add(Pair(x, y - 1))
            if (x != 0)
                add(Pair(x - 1, y))
            if (y < ySize - 1)
                add(Pair(x, y + 1))
            if (x != xSize - 1)
                add(Pair(x + 1, y))
            if (x != 0 && y != 0)
                add(Pair(x - 1, y - 1))
            if (x != 0 && y != ySize - 1)
                add(Pair(x - 1, y + 1))
            if (x != xSize - 1 && y != 0)
                add(Pair(x + 1, y - 1))
            if (x != xSize - 1 && y != ySize - 1)
                add(Pair(x + 1, y + 1))
        }
    }

    private fun performIteration() {
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, _ ->
                increaseEnergy(Pair(x, y))
            }
        }
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, _ ->
                performFlash(x, y)
            }
        }
    }

    private fun energy(x: Int, y: Int): Int {
        return data[y][x]
    }

    private fun increaseEnergy(coords: Pair<Int, Int>) {
        coords.run {
            data[second][first]++
        }
    }

    private fun performFlash(x: Int, y: Int) {
        if (energy(x, y) > 9) {
            flashes++
            resetEnergy(x, y)
            getAdjacentCoords(x, y).forEach { neighbourCoords ->
                neighbourCoords.run {
                    if (energy(neighbourCoords.first, neighbourCoords.second) != 0)
                        increaseEnergy(neighbourCoords)
                    performFlash(neighbourCoords.first, neighbourCoords.second)
                }
            }
        }
    }

    private fun resetEnergy(x: Int, y: Int) {
        data[y][x] = 0
    }

    fun printMatrix() {
        println("After step $iterations:")
        data.forEach {
            println(it.joinToString(separator = ""))
        }
        println()
    }
}