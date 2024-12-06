import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

fun main() {
    val lines = InputData.readLines("day06.txt")
    // 0,0 is the bottom left
    val grid: Map<Coordinates, Char> = lines
        .reversed()
        .flatMapIndexed { y: Int, line: String ->
            line.mapIndexed { x, c ->
                Coordinates(x, y) to c
            }
        }
        .toMap()
    val startingPosition = grid.entries.find { it.value == '^' }!!.key

    solve("Part 1", 4515) {
        calculatePath(grid, startingPosition).distinct().size
    }
    solve("Part 2", 1309) {
        val obstacleCandidates = calculatePath(grid, startingPosition).filter { it != startingPosition }.distinct()
        coroutineScope {
            obstacleCandidates
                .map { coordinates ->
                    async(Dispatchers.Default) {
                        val modifiedGrid = HashMap(grid)
                        modifiedGrid[coordinates] = '#'
                        try {
                            calculatePath(modifiedGrid, startingPosition)
                            // println("Drop $coordinates")
                            false
                        } catch (e: LoopException) {
                            // println("Found loop with $coordinates | ${Thread.currentThread().name}")
                            true
                        }
                    }
                }
                .filter { it.await() }
                .size
        }
    }
}

private fun calculatePath(grid: Map<Coordinates, Char>, startingPosition: Coordinates): List<Coordinates> {
    var position = startingPosition
    var direction = Direction.UP
    val path = mutableSetOf<Pair<Coordinates, Direction>>()
    while (grid.containsKey(position)) {
        val nextPosition = position.moveTo(direction)
        when (grid[nextPosition]) {
            '#' -> { // don't do the move and rotate
                direction = direction.rotate()
            }

            '.', '^' -> { // do the move
                if (path.contains(nextPosition to direction)) {
                    throw LoopException()
                }
                path.add(nextPosition to direction)
                position = nextPosition
            }

            null -> { // out of the grid...
                position = nextPosition
            }

            else -> throw IllegalStateException()
        }
    }
    return path.map { it.first }
}

class LoopException : Exception()

data class Coordinates(val x: Int, val y: Int) {
    fun moveTo(direction: Direction) = this.copy(x = x + direction.x, y = y + direction.y)
}

enum class Direction(val x: Int, val y: Int) {
    UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0);

    fun rotate() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }
}
