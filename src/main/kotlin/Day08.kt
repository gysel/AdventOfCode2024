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

    solve("Example part 1", 14) {
        countAntinodes(sampleGrid)
    }

    val grid: Map<Coordinates, Char> = InputData.readLines("day08.txt")
        .let(::parseCharacterGrid)

    solve("Part 1", 344) {
        countAntinodes(grid)
    }


    solve("Example part 2", 34) {
        countAntinodes(sampleGrid, true)
    }

    solve("Part 2", null) {
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
                    val leftAntinodes = mutableListOf(left)
                    var leftAntinode = left + distance
                    while (leftAntinode in gridCoordinates) {
                        leftAntinodes.add(leftAntinode)
                        leftAntinode += distance
                    }
                    val rightAntinodes = mutableListOf(right)
                    var rightAntinode = right - distance
                    while (rightAntinode in gridCoordinates) {
                        rightAntinodes.add(rightAntinode)
                        rightAntinode -= distance
                    }
                    leftAntinodes + rightAntinodes
                } else listOf(left + distance, right - distance)
            }.distinct()
    }.distinct().filter { it in gridCoordinates }.size
}
