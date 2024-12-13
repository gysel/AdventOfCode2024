import Direction.LEFT
import java.util.*

fun main() {

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
    solve("Example part 1", 1930) {
        findAreas(sampleInput).sumOf { calculateFencePrice(it, sampleInput) }
    }

    val input = InputData.readLines("day12.txt")
        .let(::parseCharacterGrid)
    solve("Part 1", 1477762) {
        findAreas(input).sumOf { calculateFencePrice(it, input) }
    }

    solve("Example part 2", 1206) {
        findAreas(sampleInput).sumOf { calculateFencePrice(it, sampleInput, true) }
    }

    solve("Part 2", 923480) {
        val areas = findAreas(input)
        areas.sumOf { calculateFencePrice(it, input, true) }
    }

}

fun findAreas(grid: Map<Coordinates, Char>): List<List<Coordinates>> {
    val result = mutableListOf<List<Coordinates>>()
    val toProcess = LinkedList<Coordinates>()
    toProcess.addAll(grid.keys)
    do {
        val c = toProcess.removeFirst()
        val plant = grid[c]!!
        // println("Processing area of $plant (${toProcess.size} areas remaining to process)")
        val areasWithSamePlant = grid.filter { it.value == plant }.keys.toMutableSet()
        areasWithSamePlant.remove(c)
        val area = mutableSetOf<Coordinates>(c)
        var currentAreas = listOf(c)

        while (currentAreas.isNotEmpty()) {
            currentAreas = currentAreas.flatMap { it.neighbours() }
                .distinct()
                .filter { it !in area }
                .filter { it in areasWithSamePlant }
                .also(area::addAll)
        }

        result.add(area.toList())
        toProcess.removeAll(area)
    } while (toProcess.isNotEmpty())
    return result.toList()
}

fun calculateFencePrice(area: List<Coordinates>, grid: Map<Coordinates, Char>, discount: Boolean = false): Long {
    val fences: List<Pair<Coordinates, Direction>> = area.flatMap { c ->
        Direction.entries.mapNotNull { direction ->
            val neighbour = c.moveTo(direction)
            if (neighbour !in area) {
                c to direction
            } else null
        }
    }
    return area.size.toLong() * if (discount) {
        //calculate number of sides
        fences.groupBy { it.second }.map { (direction: Direction, fences: List<Pair<Coordinates, Direction>>) ->
            val sideCount = fences
                .groupBy { it.first.getRelevantDimension(direction) }
                .map { (_, lineCandidates) -> lineCandidates.map { it.first.getRelevantDimension(direction.rotate()) } }
                .sumOf(::countGaps)
            sideCount
        }.sum()
    } else {
        fences.size
    }
}

fun countGaps(numbers: List<Int>): Int {
    var sides = 1
    numbers.sorted().let { sorted ->
        var current = sorted.first()
        sorted.drop(1).forEach { i ->
            if (i > current + 1) {
                sides++
            }
            current = i
        }
    }
    return sides
}

private fun Coordinates.getRelevantDimension(direction: Direction): Int {
    return if (direction == LEFT || direction == Direction.RIGHT) this.x.toInt() else this.y.toInt()
}