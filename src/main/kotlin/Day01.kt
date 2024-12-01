import kotlin.math.absoluteValue

fun main() {
    val lines = InputData.readLines("day01.txt")
    val pairs = lines.map { line ->
        line.split("   ").map { it.toInt() }
    }
    val left = pairs.map { it.first() }.sorted()
    val right = pairs.map { it.last() }.sorted()

    solve("Part 1", 2815556) {
        left.zip(right)
            .map { (a, b) -> a - b }
            .sumOf { it.absoluteValue }
    }
    solve("Part 2", 23927637) {
        left.sumOf { i ->
            right.count { it == i } * i
        }
    }

}