fun main() {

    val input = readFileAsLine("Day06")
        .split(",")
        .map { it.toInt() }

    var population = input.groupBy { it }.map { Pair(it.key, it.value.count().toULong()) }.toMutableList()

    repeat(simulateDays) { repeat ->
        val newItems = mutableListOf<Pair<Int, ULong>>()

        population.forEachIndexed { index, it ->
            if (it.first == itemBornDay) {
                population[index] = population[index].copy(maxItemDay, it.second)
                newItems.add(Pair(newItemStartDay, it.second))
            } else {
                population[index] = population[index].copy(it.first - 1, it.second)
            }
        }
        population += newItems
        population = population.groupBy { it.first }.map { Pair(it.key, it.value.sumOf { pair -> pair.second }) }.toMutableList()
        println("Day $repeat population $population")
    }
    println(population.sumOf { it.second })
}

const val simulateDays = 256

const val itemBornDay = 0

const val maxItemDay = 6

const val newItemStartDay = 8