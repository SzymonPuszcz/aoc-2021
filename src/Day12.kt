fun main() {
    val input: List<String> = readInput("Day12")

    val graph = input.map { line ->
        line.split("-")
            .let {
                Pair(Vertex(it[0]), Vertex(it[1]))
            }
    }.let {
        Graph(it)
    }
//    println(graph.countPaths())
    println(graph.countExtraPaths())
}

private class Graph(vertexes: List<Pair<Vertex, Vertex>>) {

    val adjacency: Map<Vertex, List<Vertex>>

    init {
        adjacency = (vertexes + vertexes.map { Pair(it.second, it.first) })
            .groupBy { it.first }
            .mapValues { it.value.map { (_, dest) -> dest } }
    }

    fun countPaths(): Int {
        val allPaths = mutableListOf<List<Vertex>>()
        findPaths(Vertex("start"), mutableListOf(), allPaths)
        return allPaths.count()
    }

    private fun findPaths(vertex: Vertex, currentPath: List<Vertex>, allPaths: MutableList<List<Vertex>>) {
        val path = currentPath.toMutableList().apply { add(vertex) }.toList()
        if (vertex.name == "end") {
            allPaths.add(path)
            return
        }
        adjacency.getValue(vertex)
            .mapNotNull {
                return@mapNotNull if (it.isSmall() && path.contains(it)) {
                    null
                } else {
                    findPaths(it, path, allPaths)
                }
            }
    }

    fun countExtraPaths(): Int {
        val allPaths = mutableListOf<List<Vertex>>()
        findExtraPaths(Vertex("start"), mutableListOf(), allPaths)
        return allPaths.count()
    }

    private fun findExtraPaths(vertex: Vertex, currentPath: List<Vertex>, allPaths: MutableList<List<Vertex>>) {
        val path = currentPath.toMutableList().apply { add(vertex) }.toList()
        if (vertex.name == "end") {
            allPaths.add(path)
            return
        }
        adjacency.getValue(vertex)
            .forEach {
                if (it.name == "start") {
                    return@forEach
                } else if (it.isSmall() && path.contains(it) && path
                        .filter { it.isSmall() }
                        .groupBy { it }
                        .any { it.value.size != 1 }
                ) {
                    return@forEach
                } else {
                    findExtraPaths(it, path, allPaths)
                }
            }
    }
}

data class Vertex(
    val name: String
) {
    fun isSmall() =
        name == name.lowercase()
}