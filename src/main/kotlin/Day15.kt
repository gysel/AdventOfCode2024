fun main() {
    val input = InputData.read("day15.txt").let(::parse)
    solve("Small sample part 1", 2028) {
        val (grid, movements) = """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            <^^>>>vv<v>>v<<
        """.trimIndent().let(::parse)
        move(grid, movements).let(::calculateGpsCoordinatesOfAllBoxes)
    }
    solve("Part 1", 1442192) {
        val (grid, movements) = input
        move(grid, movements).let(::calculateGpsCoordinatesOfAllBoxes)
    }
    solve("Part 2", null) {
    }

}

private fun parse(input: String): Pair<Map<Coordinates, Char>, List<Char>> {
    val (grid, movements) = input.split(("\n\n"))

    return parseCharacterGrid(grid.lines(), true) to movements.lines().joinToString("").map { it }
}

private fun move(grid: Map<Coordinates, Char>, movements: List<Char>): Map<Coordinates, Char> {
    val result = grid.toMutableMap()
    movements.forEach { move ->
        when (move) {
            '<' -> move(-1 to 0, result)
            '^' -> move(0 to -1, result)
            '>' -> move(1 to 0, result)
            'v' -> move(0 to 1, result)
            else -> error("$move is not yet implemented")
        }

        // println("\nMove $move:")
        // printMap(result, 8 to 8)
    }
    return result
}

fun move(move: Pair<Int, Int>, grid: MutableMap<Coordinates, Char>) {
    val robot = grid.entries.find { it.value == '@' }!!.key
    val moveTo = robot + move
    when (grid[moveTo]) {
        '.' -> {
            // just move robot
            grid[moveTo] = '@'
            grid[robot] = '.'
        }

        'O' -> {
            // try to move boxes
            val moveableBoxes = findMoveableBoxes(grid, moveTo, move)
            if (moveableBoxes.isNotEmpty()) {
                moveableBoxes.forEach {
                    grid[it + move] = 'O'
                }
                grid[moveTo] = '@'
                grid[robot] = '.'
            }
        }

        '#' -> {
            // path is blocked, do nothing
            // find stack of boxes, move them all
        }

        else -> error("Cell is ${grid[moveTo]}")
    }
}

fun findMoveableBoxes(
    grid: MutableMap<Coordinates, Char>,
    start: Coordinates,
    move: Pair<Int, Int>
): List<Coordinates> {
    var position = start
    val boxes = mutableListOf<Coordinates>(start)
    do {
        boxes.add(position)
        position += move
    } while (grid[position] == 'O')
    return if (grid[position] == '.') {
        boxes
    } else {
        emptyList()
    }
}

private fun calculateGpsCoordinatesOfAllBoxes(finishedGrid: Map<Coordinates, Char>) =
    finishedGrid.entries.sumOf { (c, o) -> if (o == 'O') c.y * 100 + c.x else 0 }

operator fun Coordinates.plus(other: Pair<Int, Int>) = Coordinates(x + other.first, y + other.second)
