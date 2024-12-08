fun main() {

    val sampleGrid = """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
        """.trimIndent().lines()
        .let(::parseCharacterGrid)

    val grid = InputData.readLines("day08.txt")
        .let(::parseCharacterGrid)

    // Part 1
    solve("Example part 1", 14) {
        countAntinodes(sampleGrid)
    }
    solve("Part 1", 344) {
        countAntinodes(grid)
    }
    // Part 2
    solve("Example part 2", 34) {
        countAntinodes(sampleGrid, true)
    }
    solve("Part 2", 1182) {
        countAntinodes(grid, true)
    }
}

private fun countAntinodes(grid: Map<Coordinates, Char>, repeatAntinodes: Boolean = false): Int {
    val frequencies = grid.values.filter { it != '.' }.distinct()
    val gridCoordinates = grid.keys
    return frequencies.flatMap { frequency ->
        val antennas = grid.entries.filter { it.value == frequency }.map { it.key }
        antennas.cartesianProduct(antennas)
            .filter { (left, right) -> left != right }
            .flatMap { (left, right) ->
                val distance = left - right
                if (repeatAntinodes) {
                    listOf(
                        left to Coordinates::plus,
                        right to Coordinates::minus
                    ).flatMap { (start, operation) ->
                        val antinodes = mutableListOf<Coordinates>()
                        var position = start
                        while (position in gridCoordinates) {
                            antinodes.add(position)
                            position = operation(position, distance)
                        }
                        antinodes
                    }.distinct()
                } else listOf(left + distance, right - distance)
            }.distinct()
    }.distinct().count { it in gridCoordinates }
}
