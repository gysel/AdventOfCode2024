import java.util.LinkedList

fun main() {
    run { // Examples
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
        val samplePathWithoutCheats = sampleGrid.findShortestPath()

        solve("Example part 1", 1) {
            val cheatsAndTheirSavings = sampleGrid.findCheats()
                .map { cheat -> cheat to calculateSavings(samplePathWithoutCheats, cheat) }
                .filter { it.second > 0 }

            val result = cheatsAndTheirSavings.groupBy { it.second }
            print(result)
            result[64]?.size
        }

        solve("Example part 2", 3) {
            val cheatsAndTheirSavings = sampleGrid.findCheats(20)
                .map { cheat -> cheat to calculateSavings(samplePathWithoutCheats, cheat) }
                .filter { it.second > 0 }
            val result = cheatsAndTheirSavings.groupBy { it.second }
            print(result)
            result[76]?.size
        }
    }

    val grid = InputData.readLines("day20.txt").let(::parseCharacterGrid)
    val pathWithoutCheats = grid.findShortestPath()
    solve("Part 1", 1438) {
        grid.findCheats()
            .count { cheat -> calculateSavings(pathWithoutCheats, cheat) >= 100 }
    }

    solve("Part 2", 1026446) {
        grid.findCheats(20)
            .count { cheat -> calculateSavings(pathWithoutCheats, cheat) >= 100 }
    }

}

private fun Map<Coordinates, Char>.findShortestPath(): Map<Coordinates, Int> {
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
    return paths.minBy { it.size }.withIndex().associate { (i, c) -> c to i }
}

private fun Map<Coordinates, Char>.findCheats(cheatLength: Int = 2): List<Pair<Coordinates, Coordinates>> {
    val fromChars = setOf('.', 'S')
    val toChars = setOf('.', 'E')
    return if (cheatLength == 2) {
        val walls: List<Coordinates> = this.entries.filter { it.value == '#' }.map { it.key }
        walls.flatMap {
            listOf(
                it.moveTo(Direction.UP) to it.moveTo(Direction.DOWN),
                it.moveTo(Direction.DOWN) to it.moveTo(Direction.UP),
                it.moveTo(Direction.LEFT) to it.moveTo(Direction.RIGHT),
                it.moveTo(Direction.RIGHT) to it.moveTo(Direction.LEFT)
            )
        }.distinct().filter { (from, to) ->
            this[from] in fromChars && this[to] in toChars
        }
    } else {
        val from: List<Coordinates> = this.entries.filter { it.value in fromChars }.map { it.key }
        from.flatMap { c ->
            this.entries
                .filter { it.value in toChars && it.key.distanceTo(c) <= cheatLength }
                .map { c to it.key }
        }
    }
}

fun calculateSavings(pathWithoutCheats: Map<Coordinates, Int>, cheat: Pair<Coordinates, Coordinates>): Int {
    val (from, to) = cheat
    return pathWithoutCheats[to]!! - pathWithoutCheats[from]!! - cheat.first.distanceTo(cheat.second)
        .toInt()
}

private fun Map<Coordinates, Char>.findStart() =
    entries.first { (_, v) -> v == 'S' }.key

private fun Map<Coordinates, Char>.findEnd() =
    entries.first { (_, v) -> v == 'E' }.key

private fun print(r: Map<Int, List<Pair<Pair<Coordinates, Coordinates>, Int>>>) {
    r.keys.sorted().forEach { i ->
        println("There are ${r[i]?.size ?: 0} cheats that save $i picoseconds.")
    }
}
