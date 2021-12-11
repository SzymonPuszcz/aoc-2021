import java.util.*

fun main() {
    val input: List<List<Int>> = readInput("Day09")
        .map { row -> row.map { it.toString().toInt() } }
    val lowestPoints = mutableListOf<Int>()
    val basins = mutableListOf<Int>()
    Matrix09(input).run {
        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                val value = get(x, y)
                val adjacent = getAdjacent(x, y)
                if (adjacent.all { value < it }) {
                    lowestPoints.add(value)
                    basins.add(getBasin(x, y).size)
                }
            }
        }
    }
    println(lowestPoints.sum())
    println(basins.map { it.toLong() }.sortedDescending().take(3).reduce { acc, i -> acc * i })
}

private class Matrix09(
    private val data: List<List<Int>>
) {
    val xSize = data.first().size

    val ySize = data.size

    fun get(x: Int, y: Int): Int {
        return data[y][x]
    }

    private fun getAdjacentCoors(x: Int, y: Int): List<Pair<Int, Int>> {
        return buildList {
            if (y != 0)
                add(Pair(x, y - 1))
            if (x != 0)
                add(Pair(x - 1, y))
            if (y < ySize - 1)
                add(Pair(x, y + 1))
            if (x != xSize - 1)
                add(Pair(x + 1, y))
        }
    }

    fun getAdjacent(x: Int, y: Int): List<Int> {
        return buildList {
            if (y != 0)
                add(get(x, y - 1))
            if (x != 0)
                add(get(x - 1, y))
            if (y < ySize - 1)
                add(get(x, y + 1))
            if (x != xSize - 1)
                add(get(x + 1, y))
        }
    }

    fun getBasin(startX: Int, startY: Int): List<Int> {
//        println("Basin for ($startX,$startY)")
        val basin = mutableListOf<Int>()
        val visited = mutableSetOf<Pair<Int, Int>>()
        val queue: Stack<Pair<Int, Int>> = Stack<Pair<Int, Int>>().apply { push(Pair(startX, startY)) }
        while (queue.isNotEmpty()) {
            if (queue.peek() in visited) {
                queue.pop()
                continue
            }
            visited.add(queue.peek())
            val (x, y) = queue.pop()
            val value = get(x, y)
            if (value == 9)
                continue

            basin.add(value)
            getAdjacentCoors(x, y).filterNot { it in visited }
//                .also { println("Visited ($x,$y) with value=$value, adjacent $it, visited $visited") }
                .let {
                    queue.addAll(it)
                }
        }
        return basin
    }
}