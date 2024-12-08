data class Coordinates(val x: Int, val y: Int) {
    fun moveTo(direction: Direction) = this.copy(x = x + direction.x, y = y + direction.y)

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