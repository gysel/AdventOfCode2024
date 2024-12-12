import java.util.*

fun main() {

    solve("Example 1", 1930) {
        val sampleInput = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
        """.trimIndent().lines().let(::parseCharacterGrid)

        val areas = findAreas(sampleInput)
        areas.sumOf { calculateFencePrice(it, sampleInput) }
    }

    val input = InputData.readLines("day12.txt")
        .let(::parseCharacterGrid)
    solve("Part 1", null) {
        val areas = findAreas(input)
        println("Found ${areas.size} areas")
        areas.sumOf { calculateFencePrice(it, input) }
    }
    solve("Part 2", null) {
    }

}

fun findAreas(grid: Map<Coordinates, Char>): List<List<Coordinates>> {
    val result = mutableListOf<List<Coordinates>>()
    val toProcess = LinkedList<Coordinates>()
    toProcess.addAll(grid.keys)
    do {
        val c = toProcess.removeFirst()
        val plant = grid[c]!!
        val potentialNeighbours = grid.filter { it.value == plant }.keys
        val neighbours = mutableListOf<Coordinates>()
        neighbours.add(c)
        val area = mutableSetOf<Coordinates>()
        do {
            val x = neighbours.removeFirst()
            neighbours.addAll(
                x.neighbours()
                    .filter { it in potentialNeighbours }
                    .filter { it !in area }
                    .filter { grid[it] == plant })
            area.add(x)
        } while (neighbours.isNotEmpty())
        toProcess.removeAll(area)
        result.add(area.toList())
    } while (toProcess.isNotEmpty())
    return result.toList()
}

fun calculateFencePrice(area: List<Coordinates>, grid: Map<Coordinates, Char>): Long {
    val areaPlant = grid[area.first()]
    val perimeter: Long = area.sumOf { coordinate ->
        coordinate.neighbours().count { grid[it] != areaPlant }.toLong()
    }
    return perimeter * area.size
}