fun main() {
    val lines = InputData.readLines("day07.txt")

    val equations = lines.map { line ->
        val numbers = line.split(": ", " ").map { it.toLong() }
        val testValue = numbers.first()
        val inputs = numbers.drop(1)
        testValue to inputs
    }

    solve("Part 1", 4555081946288) {
        equations.filter(::isValid).sumOf { it.first }
    }
    solve("Part 2", 227921760109726) {
        equations.filter { isValid(it, true) }.sumOf { it.first }
    }

}

fun isValid(equation: Pair<Long, List<Long>>, allowConcatenation: Boolean = false): Boolean {
    val (testValue, numbers) = equation
    val possibleResults = simulate(numbers, allowConcatenation)
    return possibleResults.any { it == testValue }
}

fun simulate(numbers: List<Long>, allowConcatenation: Boolean): List<Long> {
    val result = mutableListOf<Long>()
    simulate(numbers, result, allowConcatenation)
    return result
}

fun simulate(numbers: List<Long>, result: MutableList<Long>, allowConcatenation: Boolean) {
    val (first, second) = numbers
    val remainingNumbers = numbers.drop(2)
    val addition = first + second
    val multiplication = first * second
    val concatenation = lazy { (first.toString() + second.toString()).toLong() }
    if (remainingNumbers.isEmpty()) {
        result.add(addition)
        result.add(multiplication)
        if (allowConcatenation) {
            result.add(concatenation.value)
        }
    } else {
        simulate(listOf(addition) + remainingNumbers, result, allowConcatenation)
        simulate(listOf(multiplication) + remainingNumbers, result, allowConcatenation)
        if (allowConcatenation) {
            simulate(listOf(concatenation.value) + remainingNumbers, result, allowConcatenation)
        }
    }
}
