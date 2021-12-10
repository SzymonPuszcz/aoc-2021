import java.util.*

fun main() {

    val input = readInput("Day10")

    val totalScore = input.map(::calculateErrorScore).sum()
    println(totalScore)

    val medianAutocompleteScore = input.mapNotNull(::calculateAutocompleteScore).median()

    println(medianAutocompleteScore)
}

fun calculateErrorScore(line: String): Int {
    val stack = Stack<Char>()
    line.forEach {
        if (it.isOpeningChunk()) {
            stack.push(it)
        } else if (stack.peek().toClosingChunk() == it) {
            stack.pop()
        } else {
            return it.toErrorScore()
        }
    }
    return 0
}

fun calculateAutocompleteScore(line: String): Long? {
    val stack = Stack<Char>()
    line.forEach {
        if (it.isOpeningChunk()) {
            stack.push(it.toClosingChunk())
        } else if (stack.peek() == it) {
            stack.pop()
        } else {
            return null
        }
    }
    return stack.joinToString(separator = "")
        .foldRight(0L) { bracket, acc ->
            acc * 5 + bracket.toAutocompleteScore()
        }
}

private fun Char.isOpeningChunk() =
    when (this) {
        '(', '[', '{', '<' -> true
        else -> false
    }

private fun Char.toClosingChunk() =
    when (this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw IllegalArgumentException()
    }

private fun Char.toErrorScore(): Int =
    when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException()
    }

private fun Char.toAutocompleteScore(): Int =
    when (this) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> throw IllegalArgumentException()
    }

private fun List<Long>.median() =
    sorted()[size / 2]