import kotlin.math.absoluteValue

fun main() {
    val lines = InputData.readLines("day02.txt")
    val reports = lines.map { line -> line.split(" ").map(String::toInt) }
    solve("Part 1", 510) {
        reports.count { isSafe(it) }
    }
    solve("Part 2", 553) {
        reports.count { report -> dampener(report).any { isSafe(it) } }
    }
}

fun isSafe(report: List<Int>): Boolean {
    val windowed = report.windowed(2, 1)
    val increasing = windowed.all { (left, right) ->
        left < right
    }
    val decreasing = windowed.all { (left, right) ->
        left > right
    }
    val difference = windowed.all { (left, right) ->
        val diff = (left - right).absoluteValue
        diff in 1..3
    }
    return (increasing || decreasing) && difference
}

fun dampener(report: List<Int>): Sequence<List<Int>> {
    return report.asSequence().mapIndexed { index, _ ->
        report.filterIndexed { i, _ -> i != index }
    }
}