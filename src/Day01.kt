fun main() {

    fun countIncreases(input: List<Int>) =
        input.zipWithNext()
            .filter { it.first < it.second }
            .count()

    fun part1(input: List<Int>): Int =
        countIncreases(input)

    fun part2(input: List<Int>): Int =
        input.zipInTriples()
            .map { it.sum() }
            .let {
                countIncreases(it)
            }

    val input = readInputAsInt("Day01")

    println(part1(input))

    println(part2(input))
}

private fun Triple<Int, Int, Int>.sum(): Int =
    first + second + third

private fun <T> List<T>.zipInTriples() =
    zip(drop(1))
        .zip(drop(2)) { (a, b), c ->
            Triple(a, b, c)
        }