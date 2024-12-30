data class Coordinates(val x: Long, val y: Long) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
    constructor(x: String, y: String) : this(x.toLong(), y.toLong())

    fun moveTo(direction: Direction) = this.copy(x = x + direction.x, y = y + direction.y)
    fun neighbours() = Direction.entries.map(this::moveTo)

    operator fun plus(other: Coordinates) = Coordinates(x + other.x, y + other.y)
    operator fun minus(other: Coordinates) = Coordinates(x - other.x, y - other.y)
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

fun printMap(grid: List<Coordinates>, dimensions: Pair<Int, Int>) {
    val (maxX, maxY) = dimensions
    (0..<maxY).forEach { y ->
        (0..<maxX).map { x ->
            grid.count { it.x.toInt() == x && it.y.toInt() == y }
        }.map { if (it == 0) '.' else it.digitToChar() }.joinToString("").let(::println)
    }
}

fun printMap(grid: Map<Coordinates, Char>, dimensions: Pair<Int, Int>) {
    val (maxX, maxY) = dimensions
    (0..<maxY).forEach { y ->
        (0..<maxX).map { x ->
            grid[Coordinates(x, y)] ?: ' '
        }.joinToString("").let(::println)
    }
}
