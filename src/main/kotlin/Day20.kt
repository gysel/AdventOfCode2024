import java.util.LinkedList

fun main() {
    solve("Example part 1", 1) {
        val sampleGrid = """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
        """.trimIndent().lines().let(::parseCharacterGrid)
        val pathWithoutCheats = sampleGrid.findShortestPath()
        val r = sampleGrid.findCheats()
            .map { cheat -> cheat to calculateSavings(pathWithoutCheats, cheat) }
            .filter { it.second > 0 }
            .groupBy { it.second }
        r.keys.sorted().forEach { i ->
            println("There are ${r[i]?.size ?: 0} cheats that save $i picoseconds.")
        }
        r[64]?.size
    }

    val grid = InputData.readLines("day20.txt").let(::parseCharacterGrid)
    solve("Part 1", 1438) {
        val pathWithoutCheats = grid.findShortestPath()
        val cheats = grid.findCheats()
        cheats.count { cheat -> calculateSavings(pathWithoutCheats, cheat) >= 100 }
    }

    solve("Part 2", null) {
    }

}

private fun Map<Coordinates, Char>.findShortestPath(): List<Coordinates> {
    val from = findStart()
    val to = findEnd()
    val queue: LinkedList<List<Coordinates>> = LinkedList()
    queue.add(listOf(from))
    val paths = mutableListOf<List<Coordinates>>()
    while (queue.isNotEmpty()) {
        val x = queue.removeFirst()
        for (n in x.last().neighbours()) {
            if (n == to) {
                paths.add(x + n)
            } else if (this[n] == '.' && n !in x) {
                queue.add(x + n)
            }
        }
    }
    return paths.minBy { it.size }
}

private fun Map<Coordinates, Char>.findCheats(): List<Pair<Coordinates, Coordinates>> {
    val walls: List<Coordinates> = this.entries.filter { it.value == '#' }.map { it.key }
    return walls.flatMap {
        listOf(
            it.moveTo(Direction.UP) to it.moveTo(Direction.DOWN),
            it.moveTo(Direction.DOWN) to it.moveTo(Direction.UP),
            it.moveTo(Direction.LEFT) to it.moveTo(Direction.RIGHT),
            it.moveTo(Direction.RIGHT) to it.moveTo(Direction.LEFT)
        )
    }.distinct().filter { (from, to) ->
        this[from] in setOf('.', 'S') && this[to] in setOf('.', 'E')
    }
}

fun calculateSavings(pathWithoutCheats: List<Coordinates>, cheat: Pair<Coordinates, Coordinates>): Int {
    val (from, to) = cheat
    return pathWithoutCheats.indexOf(to) - pathWithoutCheats.indexOf(from) - 2
}

private fun Map<Coordinates, Char>.findStart() =
    entries.first { (_, v) -> v == 'S' }.key

private fun Map<Coordinates, Char>.findEnd() =
    entries.first { (_, v) -> v == 'E' }.key
