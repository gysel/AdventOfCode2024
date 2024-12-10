import java.util.*

fun main() {
    solve("Sample 1", 1) {
        val firstSampleInput = """
        0123
        1234
        8765
        9876
    """.trimIndent().lines().let(::parseNumberGrid)
        findTrailheadCandidates(firstSampleInput)
            .sumOf { calculateTrailheadPaths(it, firstSampleInput).map(List<Coordinates>::last).distinct().size }
    }

    val input: Map<Coordinates, Int> = InputData.readLines("day10.txt").let(::parseNumberGrid)
    val findTrailheadCandidates = findTrailheadCandidates(input)

    solve("Part 1", 709) {
        findTrailheadCandidates
            .sumOf { calculateTrailheadPaths(it, input).map(List<Coordinates>::last).distinct().size }
    }
    solve("Part 2", 1326) {
        findTrailheadCandidates
            .sumOf { calculateTrailheadPaths(it, input).distinct().size }
    }
}

private fun findTrailheadCandidates(firstSampleInput: Map<Coordinates, Int>) =
    firstSampleInput.entries.filter { (_, v) -> v == 0 }.map { it.key }

fun calculateTrailheadPaths(position: Coordinates, grid: Map<Coordinates, Int>): List<List<Coordinates>> {
    val queue = LinkedList<List<Coordinates>>()
    queue.add(listOf(position))
    val pathsToPeaks = mutableListOf<List<Coordinates>>()
    do {
        val currentPath = queue.removeFirst()
        val currentPosition = currentPath.last()
        val nextHeight = grid[currentPosition]!! + 1
        val neighbours = Direction.entries.map(currentPosition::moveTo)
        val pathContinuations = neighbours
            .filter { grid[it] == nextHeight }
            .map { currentPath + listOf(it) }
        if (nextHeight == 9) {
            pathsToPeaks.addAll(pathContinuations)
        } else {
            queue.addAll(pathContinuations)
        }
    } while (queue.isNotEmpty())
    return pathsToPeaks
}